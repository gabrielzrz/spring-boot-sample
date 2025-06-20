package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.UUID;

/**
 * @author Zorzi
 */
public interface PersonService {

    PersonDTO findById(UUID id);

    PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable);

    PagedModel<EntityModel<PersonDTO>> findPersonByName(String name, Pageable pageable);

    Person create(PersonDTO person);

    PersonDTO update(PersonDTO person);

    void delete(UUID id);

    void disablePerson(UUID id);
}
