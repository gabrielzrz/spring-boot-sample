package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.exception.ResourceNotFoundException;
import gabrielzrz.com.github.model.Person;
import gabrielzrz.com.github.repository.PersonRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * @author Zorzi
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServiceImpl.class.getName());
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person findById(Long id){
        logger.info("Find person by ID");
        return personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
    }

    @Override
    public List<Person> findAll(){
        logger.info("Find all people");
        return personRepository.findAll();
    }

    @Override
    public Person create(Person person) {
        logger.info("Create person");
        return personRepository.save(person);
    }

    @Override
    public Person update(Person person) {
        logger.info("udpate person");

        Person entity = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + person.getId()));

        entity.setName(person.getName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return personRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        logger.info("delete person");
        Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        personRepository.delete(entity);
    }
}
