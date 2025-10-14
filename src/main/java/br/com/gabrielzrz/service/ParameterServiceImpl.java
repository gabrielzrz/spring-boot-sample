package br.com.gabrielzrz.service;

import br.com.gabrielzrz.service.contract.BranchService;
import br.com.gabrielzrz.service.contract.ParameterService;
import br.com.gabrielzrz.constants.RepositoryAdapterConstants;
import br.com.gabrielzrz.enums.EnvironmentType;
import br.com.gabrielzrz.enums.ParameterType;
import br.com.gabrielzrz.model.Parameter;
import br.com.gabrielzrz.repository.port.ParameterRepositoryPort;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Zorzi
 */
@Service
public class ParameterServiceImpl implements ParameterService {

    private final ParameterRepositoryPort parameterRepositoryPort;
    private final BranchService branchService;

    public ParameterServiceImpl(
            @Qualifier(RepositoryAdapterConstants.Jpa.PARAMETER) ParameterRepositoryPort parameterRepositoryPort,
            BranchService branchService) {
        this.parameterRepositoryPort = parameterRepositoryPort;
        this.branchService = branchService;
    }

    @Override
    public EnvironmentType getEnvironmentType() {
        return parameterRepositoryPort.getEnvironmentType()
                .filter(parameter -> StringUtils.isNotBlank(parameter.getValue()))
                .map(parameter ->  EnvironmentType.valueOf(parameter.getValue()))
                .orElse(EnvironmentType.DEV);
    }

    @Override
    public Parameter findByParameterType(ParameterType parameterType) {
        return parameterRepositoryPort.findByParameterType(parameterType).orElse(null);
    }
}
