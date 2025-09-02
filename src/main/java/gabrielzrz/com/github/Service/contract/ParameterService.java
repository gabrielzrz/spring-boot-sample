package gabrielzrz.com.github.Service.contract;

import gabrielzrz.com.github.enums.EnvironmentType;
import gabrielzrz.com.github.enums.ParameterType;
import gabrielzrz.com.github.model.Parameter;

/**
 * @author Zorzi
 */
public interface ParameterService {

    void updateByBranch();

    EnvironmentType getEnvironmentType();

    Parameter findByParameterType(ParameterType parameterType);
}
