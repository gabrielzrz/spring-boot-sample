package br.com.gabrielzrz.service;

import br.com.gabrielzrz.service.contract.BranchService;
import br.com.gabrielzrz.service.contract.ParameterService;
import br.com.gabrielzrz.constants.RepositoryAdapterConstants;
import br.com.gabrielzrz.enums.EnvironmentType;
import br.com.gabrielzrz.enums.ParameterType;
import br.com.gabrielzrz.model.Branch;
import br.com.gabrielzrz.model.Parameter;
import br.com.gabrielzrz.repository.port.ParameterRepositoryPort;
import br.com.gabrielzrz.util.LambdaUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

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
    @Transactional
    public void updateByBranch() {
        List<Branch> branches = branchService.findAll();
        branches.forEach(this::updateByBranch);
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

    private void updateByBranch(Branch branch) {
        List<Parameter> parameters = parameterRepositoryPort.findAllByBranchId(branch.getId());
        List<Parameter> parametersToSave = new ArrayList<>();
        createByBranch(branch, parameters, parametersToSave);
        parameterRepositoryPort.saveAll(parametersToSave);
    }

    private void createByBranch(Branch branch, List<Parameter> parameters, List<Parameter> parametersToSave) {
        List<ParameterType> parameterTypes = ParameterType.listAll();
        for (ParameterType parameterType : parameterTypes) {
            Parameter parameter = LambdaUtil.findOneBy(parameters, p -> Objects.equals(p.getParameterType(), parameterType));
            if (isNull(parameter)) {
                parametersToSave.add(new Parameter(parameterType, parameterType.getDefaultValue(), parameterType.getDataType(), branch));
            }
        }
    }
}
