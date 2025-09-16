package br.com.gabrielzrz.Service.contract;

import br.com.gabrielzrz.dto.PersonDTO;
import br.com.gabrielzrz.dto.response.ImportResultDTO;
import br.com.gabrielzrz.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**person
 * @author Zorzi
 */
public interface PersonService {

    PersonDTO findById(UUID id);

    PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable);

    PagedModel<EntityModel<PersonDTO>> findPersonByName(String name, Pageable pageable);

    Person create(PersonDTO person);

    PersonDTO update(PersonDTO person);

    void delete(UUID id);

    PersonDTO disablePerson(UUID id);

    ImportResultDTO massCreation(MultipartFile file);
}
