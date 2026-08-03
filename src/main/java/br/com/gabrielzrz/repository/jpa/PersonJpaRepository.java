package br.com.gabrielzrz.repository.jpa;

import br.com.gabrielzrz.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Zorzi
 */
@Repository
public interface PersonJpaRepository extends JpaRepository<Person, UUID>, JpaSpecificationExecutor<Person> {
}
