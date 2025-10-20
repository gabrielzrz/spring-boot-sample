package br.com.gabrielzrz.repository.port;

import br.com.gabrielzrz.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

/**
 * @author Zorzi
 */
public interface PersonRepositoryPort {

    Person findById(UUID id);

    Person save(Person person);

    Page<Person> findAll(Specification<Person> specification, Pageable pageable);

    Page<Person> findAll(Pageable pageable);

    List<Person> saveAll(List<Person> people);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    void disablePerson(UUID id);
}
