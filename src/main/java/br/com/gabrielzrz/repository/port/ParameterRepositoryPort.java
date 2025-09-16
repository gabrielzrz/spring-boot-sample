package br.com.gabrielzrz.repository.port;

import br.com.gabrielzrz.enums.ParameterType;
import br.com.gabrielzrz.model.Parameter;
import br.com.gabrielzrz.repository.base.BaseRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Zorzi
 */
public interface ParameterRepositoryPort extends BaseRepositoryPort<Parameter, UUID> {

    Page<Parameter> findAllByBranchId(UUID branchId, Pageable pageable);

    List<Parameter> findAllByBranchId(UUID branchId);

    Optional<Parameter> getEnvironmentType();

    Optional<Parameter> findByParameterType(ParameterType parameterType);
}
