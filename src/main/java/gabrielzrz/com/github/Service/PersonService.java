package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.model.Person;

import java.util.List;

/**
 * @author Zorzi
 */
public interface PersonService {

    Person findById(Long id);

    List<Person> findAll();

    Person create(Person person);

    Person update(Person person);

    void delete(Long id);
}
