package br.com.gabrielzrz.service;

import br.com.gabrielzrz.service.contract.FileImportService;
import br.com.gabrielzrz.service.contract.PersonService;
import br.com.gabrielzrz.constants.RepositoryAdapterConstants;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Service
public class PersonServiceImpl implements PersonService {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    private final PersonRepositoryPort personRepositoryPort;
    private final ExceptionMessageParser exceptionMessageParser;
    private final FileImportService fileImportService;
    private final PersonMapper personMapper;

    public PersonServiceImpl(
            @Qualifier(RepositoryAdapterConstants.Jpa.PERSON) PersonRepositoryPort personRepositoryPort,
            ExceptionMessageParser exceptionMessageParser,
            FileImportService fileImportService,
            PersonMapper personMapper) {
        this.personRepositoryPort = personRepositoryPort;
        this.exceptionMessageParser = exceptionMessageParser;
        this.fileImportService = fileImportService;
        this.personMapper = personMapper;
    }

    @Override
    public PersonDTO findById(UUID id) {
        existsPersonById(id);
        Person entity = personRepositoryPort.findById(id);
        return personMapper.toDTO(entity);
    }

    @Override
    public Page<PersonDTO> findAll(Pageable pageable) {
        Page<Person> people = personRepositoryPort.findAll(pageable);
        return people.map(personMapper::toDTO);
    }

    @Override
    public Page<PersonDTO> findPersonByName(String name, Pageable pageable) {
        Page<Person> people = personRepositoryPort.findPeopleByName(name, pageable);
        return people.map(personMapper::toDTO);
    }

    @Override
    public PersonDTO create(PersonDTO personDTO) {
        Person p = personMapper.toEntity(personDTO);
        return personMapper.toDTO(personRepositoryPort.save(p));
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
        return personMapper.toDTO(person);
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
}
