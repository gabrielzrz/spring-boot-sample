package gabrielzrz.com.github.repository;

import gabrielzrz.com.github.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Zorzi
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.userName = :userName")
    User findByUsername(@Param("userName") String userName);

    @Query("SELECT user FROM User user WHERE user.userName = :userName")
    Optional<User> findByUsernameOptional(@Param("userName") String userName);
}
