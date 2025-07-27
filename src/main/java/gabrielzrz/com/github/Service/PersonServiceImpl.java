package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.controllers.PersonController;
import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.dto.response.ImportErrorDTO;
import gabrielzrz.com.github.dto.response.ImportResultDTO;
import gabrielzrz.com.github.exception.ResourceNotFoundException;
import gabrielzrz.com.github.mapper.ObjectMapper;
import gabrielzrz.com.github.model.Person;
import gabrielzrz.com.github.repository.PersonRepository;
import gabrielzrz.com.github.util.ExceptionMessageParser;
import gabrielzrz.com.github.util.LambdaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static gabrielzrz.com.github.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Zorzi
 */
@Service
public class PersonServiceImpl implements PersonService {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    private final PersonRepository personRepository;
    private final PagedResourcesAssembler<PersonDTO> assembler;
    private final ExceptionMessageParser exceptionMessageParser;
    private final FileImportService fileImportService;

    public PersonServiceImpl(
            PersonRepository personRepository,
            PagedResourcesAssembler<PersonDTO> assembler,
            ExceptionMessageParser exceptionMessageParser,
            FileImportService fileImportService) {
        this.personRepository = personRepository;
        this.assembler = assembler;
        this.exceptionMessageParser = exceptionMessageParser;
        this.fileImportService = fileImportService;
    }

    @Override
    public PersonDTO findById(UUID id){
        logger.info("Find person by ID");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        return parseObject(entity, PersonDTO.class);
        //return addHateoasLink(personDto);
    }

    @Override
    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable){
        logger.info("Find all people");
        Page<Person> people = personRepository.findAll(pageable);
        Page<PersonDTO> peopleDTO = people.map(person -> {
            return addHateoasLink(parseObject(person, PersonDTO.class));
            //return ObjectMapper.parseObject(person, PersonDTO.class);
        });
        return assembler.toModel(peopleDTO, createLinkHAL(pageable));
    }

    @Override
    public PagedModel<EntityModel<PersonDTO>> findPersonByName(String name, Pageable pageable) {
        logger.info("Find people by name");
        Page<Person> people = personRepository.findPeopleByName(name, pageable);
        Page<PersonDTO> peopleDTO = people.map(person -> {
            //return addHateoasLink(ObjectMapper.parseObject(person, PersonDTO.class));
            return parseObject(person, PersonDTO.class);
        });
        return assembler.toModel(peopleDTO, createLinkHAL(pageable));
    }

    @Override
    public Person create(PersonDTO person) {
        logger.info("Create person");
        Person p = parseObject(person, Person.class);
        return personRepository.save(p);
    }

    @Override
    public PersonDTO update(PersonDTO person) {
        logger.info("update person");
        Person entity = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + person.getId()));
        entity.setName(person.getName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return parseObject(personRepository.save(entity), PersonDTO.class);
    }

    @Override
    public void delete(UUID id) {
        logger.info("delete person");
        Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        personRepository.delete(entity);
    }

    @Override
    public ImportResultDTO massCreation(MultipartFile file) {
        return fileImportService.importFile(file, PersonDTO.class, this::saveImportedPeople);
    }

    @Override
    public PersonDTO disablePerson(UUID id) {
        logger.info("Disabling one Person!");
        Person person = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        personRepository.disabledPerson(id);
        PersonDTO dto = parseObject(person, PersonDTO.class);
        return addHateoasLink(dto);
    }

    private void saveImportedPeople(List<PersonDTO> items, ImportResultDTO result) {
        List<Person> people = LambdaUtil.mapTo(items, dto -> ObjectMapper.parseObject(dto, Person.class));
        for (Person person : people) {
            try {
                personRepository.save(person);
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
