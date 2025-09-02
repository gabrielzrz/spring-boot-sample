package gabrielzrz.com.github.repository.port;

import gabrielzrz.com.github.enums.ParameterType;
import gabrielzrz.com.github.model.Parameter;
import gabrielzrz.com.github.repository.base.BaseRepositoryPort;
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
