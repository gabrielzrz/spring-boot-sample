package br.com.gabrielzrz.service.contract;

import br.com.gabrielzrz.enums.EnvironmentType;
import br.com.gabrielzrz.enums.ParameterType;
import br.com.gabrielzrz.model.Parameter;

/**
 * @author Zorzi
 */
public interface ParameterService {

    void updateByBranch();

    EnvironmentType getEnvironmentType();

    Parameter findByParameterType(ParameterType parameterType);
}
