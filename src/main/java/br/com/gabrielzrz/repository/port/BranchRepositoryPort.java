package br.com.gabrielzrz.repository.port;

import br.com.gabrielzrz.model.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Zorzi
 */
public interface BranchRepositoryPort {

    Branch findById(UUID uuid);

    Page<Branch> findAll(Pageable pageable);

    Branch save(Branch branch);

    boolean existsById(UUID uuid);

    List<Branch> findAll();
}
