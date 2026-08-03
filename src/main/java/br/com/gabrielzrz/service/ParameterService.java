package br.com.gabrielzrz.service;

import br.com.gabrielzrz.enums.EnvironmentType;
import br.com.gabrielzrz.enums.ParameterType;
import br.com.gabrielzrz.entity.Parameter;
import br.com.gabrielzrz.repository.jpa.ParameterRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Zorzi
 */
@Service
public class ParameterService {

    private final ParameterRepository parameterRepository;

    public ParameterService(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    public EnvironmentType getEnvironmentType() {
        return parameterRepository.findByParameterType(ParameterType.SYSTEM_ENVIRONMENT_TYPE)
                .filter(parameter -> StringUtils.isNotBlank(parameter.getValue()))
                .map(parameter ->  EnvironmentType.valueOf(parameter.getValue()))
                .orElse(EnvironmentType.DEV);
    }

    public Parameter findByParameterType(ParameterType parameterType) {
        return parameterRepository.findByParameterType(parameterType).orElse(null);
    }
}
