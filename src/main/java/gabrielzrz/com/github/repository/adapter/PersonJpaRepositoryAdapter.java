package gabrielzrz.com.github.repository.adapter;

import gabrielzrz.com.github.constants.RepositoryAdapterConstants;
import gabrielzrz.com.github.model.Person;
import gabrielzrz.com.github.repository.jpa.PersonJpaRepository;
import gabrielzrz.com.github.repository.port.PersonRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Component(RepositoryAdapterConstants.Jpa.PERSON)
public class PersonJpaRepositoryAdapter implements PersonRepositoryPort {

    private PersonJpaRepository personJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public PersonJpaRepositoryAdapter(PersonJpaRepository personJpaRepository) {
        this.personJpaRepository = personJpaRepository;
    }

    @Override
    public Person findById(UUID id) {
        return personJpaRepository.findById(id).orElse(null);
    }

    @Override
    public Person save(Person person) {
        return personJpaRepository.save(person);
    }

    @Override
    public List<Person> saveAll(List<Person> people) {
         return personJpaRepository.saveAll(people);
    }

    @Override
    public void delete(Person person) {
        personJpaRepository.delete(person);
    }

    @Override
    public void deleteById(UUID id) {
        personJpaRepository.deleteById(id);
    }

    @Override
    public int disablePerson(UUID id) {
        return personJpaRepository.disablePerson(id);
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
        return personJpaRepository.findAll(pageable);
    }

    @Override
    public List<Person> findAll() {
        return personJpaRepository.findAll();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return personJpaRepository.existsById(uuid);
    }

    @Override
    public Page<Person> findPeopleByName(String name, Pageable pageable) {
        return personJpaRepository.findPeopleByName(name, pageable);
    }
}
