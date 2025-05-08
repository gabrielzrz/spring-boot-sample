package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.controllers.TestLogController;
import gabrielzrz.com.github.dto.v1.PersonDTO;
import gabrielzrz.com.github.dto.v2.PersonDTOV2;
import gabrielzrz.com.github.exception.ResourceNotFoundException;
import gabrielzrz.com.github.mapper.ObjectMapper;
import gabrielzrz.com.github.model.Person;
import gabrielzrz.com.github.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Zorzi
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName());
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonDTO findById(Long id){
        logger.info("Find person by ID");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        return ObjectMapper.parseObject(entity, PersonDTO.class);
    }

    @Override
    public List<PersonDTO> findAll(){
        logger.info("Find all people");
        return ObjectMapper.parseListObject(personRepository.findAll(), PersonDTO.class);
    }

    @Override
    public PersonDTO create(PersonDTO person) {
        logger.info("Create person");
        Person p = ObjectMapper.parseObject(person, Person.class);
        var entity = personRepository.save(p);
        return ObjectMapper.parseObject(entity, PersonDTO.class);
    }

    @Override
    public Person create(PersonDTOV2 person) {
        logger.info("Create person V2");
        Person p = ObjectMapper.parseObject(person, Person.class);
        return personRepository.save(p);
    }

    @Override
    public PersonDTO update(PersonDTO person) {
        logger.info("update person");
        Person entity = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + person.getId()));
        entity.setName(person.getName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return ObjectMapper.parseObject(personRepository.save(entity), PersonDTO.class);
    }

    @Override
    public void delete(Long id) {
        logger.info("delete person");
        Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        personRepository.delete(entity);
    }
}
