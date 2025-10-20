package br.com.gabrielzrz.repository.port;

import br.com.gabrielzrz.enums.ParameterType;
import br.com.gabrielzrz.model.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Zorzi
 */
public interface ParameterRepositoryPort {

    Parameter findById(UUID uuid);

    Page<Parameter> findAll(Pageable pageable, UUID branchId);

    boolean existsById(UUID uuid);

    Parameter save(Parameter entity);

    Optional<Parameter> getEnvironmentType();

    Optional<Parameter> findByParameterType(ParameterType parameterType);
}
