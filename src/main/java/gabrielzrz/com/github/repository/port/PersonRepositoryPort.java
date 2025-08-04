package gabrielzrz.com.github.repository.port;

import gabrielzrz.com.github.model.Person;
import gabrielzrz.com.github.repository.base.BaseRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Zorzi
 */
public interface PersonRepositoryPort extends BaseRepositoryPort<Person, UUID> {

    int disablePerson(UUID id);

    Page<Person> findPeopleByName(String name, Pageable pageable);
}
