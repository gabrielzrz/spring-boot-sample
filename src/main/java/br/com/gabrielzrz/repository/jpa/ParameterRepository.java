package br.com.gabrielzrz.repository.jpa;

import br.com.gabrielzrz.enums.ParameterType;
import br.com.gabrielzrz.model.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Repository
public interface ParameterRepository extends JpaRepository<Parameter, UUID> {

    Page<Parameter> findAllByBranchId(UUID branchId, Pageable pageable);

    List<Parameter> findAllByBranchId(UUID branchId);

    Optional<Parameter> findByParameterType(ParameterType parameterType);
}
