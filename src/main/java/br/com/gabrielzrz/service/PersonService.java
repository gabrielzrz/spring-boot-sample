package br.com.gabrielzrz.service;

import br.com.gabrielzrz.dto.request.filters.PersonFilterRequest;
import br.com.gabrielzrz.dto.request.PersonRequestDTO;
import br.com.gabrielzrz.dto.response.PersonResponseDTO;
import br.com.gabrielzrz.repository.specification.PersonSpecification;
import br.com.gabrielzrz.dto.response.ImportErrorDTO;
import br.com.gabrielzrz.dto.response.ImportResultDTO;
import br.com.gabrielzrz.exception.ResourceNotFoundException;
import br.com.gabrielzrz.mapper.PersonMapper;
import br.com.gabrielzrz.entity.Person;
import br.com.gabrielzrz.repository.jpa.PersonJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Service
public class PersonService {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    private final ExceptionMessageParserService exceptionMessageParserService;
    private final PersonJpaRepository personJpaRepository;
    private final FileImportService fileImportService;
    private final PersonMapper personMapper;

    public PersonService(
            ExceptionMessageParserService exceptionMessageParserService,
            PersonJpaRepository personJpaRepository,
            FileImportService fileImportService,
            PersonMapper personMapper) {
        this.exceptionMessageParserService = exceptionMessageParserService;
        this.personJpaRepository = personJpaRepository;
        this.fileImportService = fileImportService;
        this.personMapper = personMapper;
    }

    @Cacheable(value = "person", key = "#id")
    public PersonResponseDTO findById(UUID id) {
        Person entity = personJpaRepository.findById(id).orElse(null);
        return personMapper.toResponse(entity);
    }

    public Page<PersonResponseDTO> findAll(PersonFilterRequest personFilterRequest, Pageable pageable) {
        Specification<Person> spec = PersonSpecification.withFilters(personFilterRequest);
        Page<Person> people = personJpaRepository.findAll(spec, pageable);
        return people.map(personMapper::toResponse);
    }

    public Page<PersonResponseDTO> findAll(Pageable pageable) {
        Page<Person> people = personJpaRepository.findAll(pageable);
        return people.map(personMapper::toResponse);
    }

    @CachePut(value = "person", key = "#result.id")
    public PersonResponseDTO create(PersonRequestDTO personRequestDTO) {
        Person p = personMapper.toEntity(personRequestDTO);
        return personMapper.toResponse(personJpaRepository.save(p));
    }

    @CacheEvict(value = "person", key = "#personRequestDTO.id")
    public void update(PersonRequestDTO personRequestDTO) {
        personJpaRepository.save(personMapper.toEntity(personRequestDTO));
    }

    @CacheEvict(value = "person", key = "#id")
    public void delete(UUID id) {
        personJpaRepository.deleteById(id);
    }

    @CachePut(value = "person", key = "#id")
    public void disablePerson(UUID id) {
        Person person = personJpaRepository.findById(id).orElse(null);
        if (person != null) {
            person.setEnabled(false);
            personJpaRepository.save(person);
        }
    }

    public ImportResultDTO massCreation(MultipartFile file) {
        return fileImportService.importFile(file, PersonRequestDTO.class, this::saveImportedPeople);
    }

    private void saveImportedPeople(List<PersonRequestDTO> items, ImportResultDTO result) {
        List<Person> people = personMapper.toEntity(items);
        try {
            int sizeSave = personJpaRepository.saveAll(people).size();
            result.setSuccessfulImports(sizeSave);
        } catch (Exception exception) {
            logger.warn("Batch people insert failed, switching to Individually mode: {}", exception.getMessage());
            processImportedPersonIndividually(people, result);
        }
    }

    private void processImportedPersonIndividually(List<Person> people, ImportResultDTO result) {
        for (Person person : people) {
            try {
                personJpaRepository.save(person);
                result.incrementSuccessful();
            } catch (DataIntegrityViolationException e) {
                // Violação de integridade (chave duplicada, FK, etc.)
                result.incrementFailed();
                String errorMessage = exceptionMessageParserService.parseDataIntegrityError(e);
                result.addError(new ImportErrorDTO(errorMessage, person.getName()));
            } catch (jakarta.validation.ConstraintViolationException e) {
                // Violação de validação Bean Validation (@NotNull, @Size, etc.)
                result.incrementFailed();
                String errorMessage = exceptionMessageParserService.parseValidationErrors(e.getConstraintViolations());
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
