package gabrielzrz.com.github.repository;

import gabrielzrz.com.github.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person p SET p.enabled = FALSE WHERE p.id = :id")
    void disabledPerson(@Param("id") Long id);

    @Query("SELECT p FROM Person p WHERE UPPER(p.name) LIKE UPPER(CONCAT('%',:name,'%'))")
    Page<Person> findPeopleByName(@Param("name") String name, Pageable pageable);
}
