package br.com.gabrielzrz.config;

import br.com.gabrielzrz.Service.contract.ParameterService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * @author Zorzi
 */
@Component
public class InitConfig {

    private final ParameterService parameterService;

    // Constructors
    public InitConfig(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    // Init
    @PostConstruct
    public void init() {
        parameterService.updateByBranch();
    }
}
