package br.com.gabrielzrz.repository.adapter;

import br.com.gabrielzrz.constants.RepositoryAdapterConstants;
import br.com.gabrielzrz.model.Person;
import br.com.gabrielzrz.repository.jpa.PersonJpaRepository;
import br.com.gabrielzrz.repository.port.PersonRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Component(RepositoryAdapterConstants.Jpa.PERSON)
public class PersonJpaRepositoryAdapter implements PersonRepositoryPort {

    private final PersonJpaRepository personJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public PersonJpaRepositoryAdapter(PersonJpaRepository personJpaRepository) {
        this.personJpaRepository = personJpaRepository;
    }

    @Override
    @Cacheable(value = "person", key = "#id")
    public Person findById(UUID id) {
        return personJpaRepository.findById(id).orElse(null);
    }

    @Override
    @CachePut(value = "person", key = "#result.id")
    public Person save(Person person) {
        return personJpaRepository.save(person);
    }

    @Override
    public Page<Person> findAll(Specification<Person> specification, Pageable pageable) {
        return personJpaRepository.findAll(specification, pageable);
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
        return personJpaRepository.findAll(pageable);
    }

    @Override
    public List<Person> saveAll(List<Person> people) {
         return personJpaRepository.saveAll(people);
    }

    @Override
    @CacheEvict(value = "person", key = "#id")
    public void deleteById(UUID id) {
        personJpaRepository.deleteById(id);
    }

    @Override
    @CachePut(value = "person", key = "#person.id")
    public void disablePerson(UUID id) {
        personJpaRepository.disablePerson(id);
    }


    @Override
    public boolean existsById(UUID uuid) {
        return personJpaRepository.existsById(uuid);
    }
}
