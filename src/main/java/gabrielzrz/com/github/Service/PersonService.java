package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.dto.v1.PersonDTO;
import gabrielzrz.com.github.dto.v2.PersonDTOV2;

import java.util.List;

/**
 * @author Zorzi
 */
public interface PersonService {

    PersonDTO findById(Long id);

    List<PersonDTO> findAll();

    PersonDTO create(PersonDTO person);

    PersonDTOV2 create(PersonDTOV2 person);

    PersonDTO update(PersonDTO person);

    void delete(Long id);
}
