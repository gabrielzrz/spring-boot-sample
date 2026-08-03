package br.com.gabrielzrz.repository.jpa;

import br.com.gabrielzrz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {

    @Query("SELECT user FROM User user WHERE user.username = :username")
    Optional<User> findByUsername(String username);
}
