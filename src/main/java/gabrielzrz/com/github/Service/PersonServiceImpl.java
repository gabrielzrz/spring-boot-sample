package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public Person findById(String id){
        logger.info("Find person by ID");
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setName("Gabriel zorzi");
        person.setAddress("rua vicente machado");
        person.setGender("Male");
        return person;
    }

    @Override
    public List<Person> findAll(){
        logger.info("Find all people");
        List<Person> people = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            Person person = new Person();
            person.setId(counter.incrementAndGet());
            person.setName("Gabriel zorzi");
            person.setAddress("rua vicente machado");
            person.setGender("Male");
            people.add(person);
        }
        return people;
    }

    @Override
    public Person create(Person person) {
        logger.info("Create person");

        return person;
    }

    @Override
    public Person update(Person person) {
        logger.info("udpate person");

        return person;
    }

    @Override
    public void delete(String id) {
        logger.info("delete person");


    }

}
