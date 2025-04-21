package gabrielzrz.com.github.repository;

import gabrielzrz.com.github.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
