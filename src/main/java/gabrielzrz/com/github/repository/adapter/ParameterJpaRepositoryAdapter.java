package gabrielzrz.com.github.repository.adapter;

import gabrielzrz.com.github.constants.RepositoryAdapterConstants;
import gabrielzrz.com.github.enums.ParameterType;
import gabrielzrz.com.github.model.Parameter;
import gabrielzrz.com.github.repository.jpa.ParameterRepository;
import gabrielzrz.com.github.repository.port.ParameterRepositoryPort;
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
    public Page<Parameter> findAll(Pageable pageable) {
        return parameterRepository.findAll(pageable);
    }

    @Override
    public List<Parameter> findAll() {
        return parameterRepository.findAll();
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
    public List<Parameter> saveAll(List<Parameter> entities) {
        return parameterRepository.saveAll(entities);
    }

    @Override
    public void delete(Parameter entity) {
        parameterRepository.delete(entity);
    }

    @Override
    public void deleteById(UUID uuid) {
        parameterRepository.deleteById(uuid);
    }

    @Override
    public Page<Parameter> findAllByBranchId(UUID branchId, Pageable pageable) {
        return parameterRepository.findAllByBranchId(branchId, pageable);
    }

    @Override
    public List<Parameter> findAllByBranchId(UUID branchId) {
        return parameterRepository.findAllByBranchId(branchId);
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
