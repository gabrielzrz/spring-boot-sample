package br.com.gabrielzrz.Service;

import br.com.gabrielzrz.Service.contract.FileImportService;
import br.com.gabrielzrz.Service.contract.PersonService;
import br.com.gabrielzrz.constants.RepositoryAdapterConstants;
import br.com.gabrielzrz.controllers.PersonController;
import br.com.gabrielzrz.dto.PersonDTO;
import br.com.gabrielzrz.dto.response.ImportErrorDTO;
import br.com.gabrielzrz.dto.response.ImportResultDTO;
import br.com.gabrielzrz.exception.ResourceNotFoundException;
import br.com.gabrielzrz.mapper.PersonMapper;
import br.com.gabrielzrz.model.Person;
import br.com.gabrielzrz.repository.port.PersonRepositoryPort;
import br.com.gabrielzrz.util.ExceptionMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Zorzi
 */
@Service
public class PersonServiceImpl implements PersonService {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    private final PersonRepositoryPort personRepositoryPort;
    private final PagedResourcesAssembler<PersonDTO> assembler;
    private final ExceptionMessageParser exceptionMessageParser;
    private final FileImportService fileImportService;
    private final PersonMapper personMapper;

    public PersonServiceImpl(
            @Qualifier(RepositoryAdapterConstants.Jpa.PERSON) PersonRepositoryPort personRepositoryPort,
            PagedResourcesAssembler<PersonDTO> assembler,
            ExceptionMessageParser exceptionMessageParser,
            FileImportService fileImportService, PersonMapper personMapper) {
        this.personRepositoryPort = personRepositoryPort;
        this.assembler = assembler;
        this.exceptionMessageParser = exceptionMessageParser;
        this.fileImportService = fileImportService;
        this.personMapper = personMapper;
    }

    @Override
    public PersonDTO findById(UUID id) {
        existsPersonById(id);
        Person entity = personRepositoryPort.findById(id);
        PersonDTO dto = personMapper.toDTO(entity);
        return addHateoasLink(dto);
    }

    @Override
    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        Page<Person> people = personRepositoryPort.findAll(pageable);
        Page<PersonDTO> peopleDTO = people.map(personMapper::toDTO);
        return assembler.toModel(peopleDTO, createLinkHAL(pageable));
    }

    @Override
    public PagedModel<EntityModel<PersonDTO>> findPersonByName(String name, Pageable pageable) {
        Page<Person> people = personRepositoryPort.findPeopleByName(name, pageable);
        Page<PersonDTO> peopleDTO = people.map(personMapper::toDTO);
        return assembler.toModel(peopleDTO, createLinkHAL(pageable));
    }

    @Override
    public Person create(PersonDTO person) {
        Person p = personMapper.toEntity(person);
        return personRepositoryPort.save(p);
    }

    @Override
    public PersonDTO update(PersonDTO personDTO) {
        existsPersonById(personDTO.getId());
        Person person = personRepositoryPort.save(personMapper.toEntity(personDTO));
        return personMapper.toDTO(person);
    }

    @Override
    public void delete(UUID id) {
        existsPersonById(id);
        Person entity = personRepositoryPort.findById(id);
        personRepositoryPort.delete(entity);
    }

    @Override
    public ImportResultDTO massCreation(MultipartFile file) {
        return fileImportService.importFile(file, PersonDTO.class, this::saveImportedPeople);
    }

    @Override
    public PersonDTO disablePerson(UUID id) {
        existsPersonById(id);
        personRepositoryPort.disablePerson(id);
        Person person = personRepositoryPort.findById(id);
        PersonDTO dto = personMapper.toDTO(person);
        return addHateoasLink(dto);
    }

    private void existsPersonById(UUID id) {
        if (!personRepositoryPort.existsById(id)) {
            throw new ResourceNotFoundException("No records found for this ID: " + id);
        }
    }

    private void saveImportedPeople(List<PersonDTO> items, ImportResultDTO result) {
        List<Person> people = personMapper.toEntity(items);
        try {
            int sizeSave = personRepositoryPort.saveAll(people).size();
            result.setSuccessfulImports(sizeSave);
        } catch (Exception exception) {
            logger.warn("Batch people insert failed, switching to Individually mode: {}", exception.getMessage());
            processImportedPersonIndividually(people, result);
        }
    }

    private void processImportedPersonIndividually(List<Person> people, ImportResultDTO result) {
        for (Person person : people) {
            try {
                personRepositoryPort.save(person);
                result.incrementSuccessful();
            } catch (DataIntegrityViolationException e) {
                // Violação de integridade (chave duplicada, FK, etc.)
                result.incrementFailed();
                String errorMessage = exceptionMessageParser.parseDataIntegrityError(e);
                result.addError(new ImportErrorDTO(errorMessage, person.getName()));
            } catch (jakarta.validation.ConstraintViolationException e) {
                // Violação de validação Bean Validation (@NotNull, @Size, etc.)
                result.incrementFailed();
                String errorMessage = exceptionMessageParser.parseValidationErrors(e.getConstraintViolations());
                result.addError(new ImportErrorDTO(errorMessage, person.getName()));
            } catch (org.hibernate.exception.ConstraintViolationException e) {
                // Violação de constraint do banco (Hibernate)
                result.incrementFailed();
                String errorMessage = "Violação de constraint: " + e.getConstraintName();
                result.addError(new ImportErrorDTO(errorMessage, person.getName()));
            } catch (DataAccessException e) {
                // Exceções gerais de acesso a dados (Spring)
                result.incrementFailed();
                String errorMessage = "Erro de acesso aos dados: " + e.getMostSpecificCause().getMessage();
                result.addError(new ImportErrorDTO(errorMessage, person.getName()));
            } catch (Exception e) {
                result.incrementFailed();
                result.addError(new ImportErrorDTO("Erro inesperado: " + e.getMessage(), person.getName()));
            }
        }
    }

    private PersonDTO addHateoasLink(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(null, null, "ASC")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).udpate(dto)).withRel("update").withType("PUT"));
        return dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

    private Link createLinkHAL(Pageable pageable) {
        return WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                .methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort())))
                .withSelfRel();
    }
}
