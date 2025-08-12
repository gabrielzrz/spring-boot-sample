package gabrielzrz.com.github.repository.jpa;

import gabrielzrz.com.github.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Zorzi
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {
}
