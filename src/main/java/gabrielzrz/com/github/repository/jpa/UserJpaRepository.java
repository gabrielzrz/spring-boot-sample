package gabrielzrz.com.github.repository.jpa;

import gabrielzrz.com.github.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Zorzi
 */
public interface UserJpaRepository extends JpaRepository<User, UUID> {

    @Query("SELECT user FROM User user WHERE user.userName = :userName")
    User findByUsername(@Param("userName") String userName);

    @Query("SELECT user FROM User user WHERE user.userName = :userName")
    Optional<User> findByUsernameOptional(@Param("userName") String userName);
}
