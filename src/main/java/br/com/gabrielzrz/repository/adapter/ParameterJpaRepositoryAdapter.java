package br.com.gabrielzrz.repository.adapter;

import br.com.gabrielzrz.constants.RepositoryAdapterConstants;
import br.com.gabrielzrz.enums.ParameterType;
import br.com.gabrielzrz.model.Parameter;
import br.com.gabrielzrz.repository.jpa.ParameterRepository;
import br.com.gabrielzrz.repository.port.ParameterRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Component(RepositoryAdapterConstants.Jpa.PARAMETER)
public class ParameterJpaRepositoryAdapter implements ParameterRepositoryPort {

    private final ParameterRepository parameterRepository;

    public ParameterJpaRepositoryAdapter(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    @Override
    public Parameter findById(UUID uuid) {
        return parameterRepository.findById(uuid).orElse(null);
    }

    @Override
    public Page<Parameter> findAll(Pageable pageable, UUID branchId) {
        return parameterRepository.findAll(pageable);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return parameterRepository.existsById(uuid);
    }

    @Override
    public Parameter save(Parameter entity) {
        return parameterRepository.save(entity);
    }

    @Override
    public Optional<Parameter> getEnvironmentType() {
        return parameterRepository.findByParameterType(ParameterType.SYSTEM_ENVIRONMENT_TYPE);
    }

    @Override
    public Optional<Parameter> findByParameterType(ParameterType parameterType) {
        return parameterRepository.findByParameterType(parameterType);
    }
}
