package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.model.Person;

import java.util.List;

/**
 * @author Zorzi
 */
public interface PersonService {

    PersonDTO findById(Long id);

    List<PersonDTO> findAll();

    PersonDTO create(PersonDTO person);

    PersonDTO update(PersonDTO person);

    void delete(Long id);
}
