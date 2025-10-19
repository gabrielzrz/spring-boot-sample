package br.com.gabrielzrz.service.contract;

import br.com.gabrielzrz.dto.PersonDTO;
import br.com.gabrielzrz.dto.response.ImportResultDTO;
import br.com.gabrielzrz.model.Person;
import org.springframework.data.domain.Page;
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

    Page<PersonDTO> findAll(Pageable pageable);

    Page<PersonDTO> findPersonByName(String name, Pageable pageable);

    PersonDTO create(PersonDTO person);

    PersonDTO update(PersonDTO person);

    void delete(UUID id);

    PersonDTO disablePerson(UUID id);

    ImportResultDTO massCreation(MultipartFile file);
}
