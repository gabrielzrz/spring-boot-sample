package br.com.gabrielzrz.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author Zorzi
 */
public enum ParameterType {

    SYSTEM_ENVIRONMENT_TYPE(DataType.TEXT, ParameterModule.SYSTEM, EnvironmentType.DEV.toString()),
    SYSTEM_DISCORD_WEBHOOK_URL(DataType.TEXT, ParameterModule.SYSTEM);

    // Variables
    private final DataType dataType;
    private final ParameterModule parameterModule;
    private final String defaultValue;

    // Constructor
    ParameterType(DataType dataType, ParameterModule parameterModule, String defaultValue) {
        this.dataType = dataType;
        this.parameterModule = parameterModule;
        this.defaultValue = defaultValue;
    }

    ParameterType(DataType dataType, ParameterModule parameterModule) {
        this.dataType = dataType;
        this.parameterModule = parameterModule;
        this.defaultValue = null;
    }

    public static List<ParameterType> listAll() {
        return Arrays.asList(ParameterType.values());
    }

    // Getters
    public String getDefaultValue() {
        return defaultValue;
    }

    public DataType getDataType() {
        return dataType;
    }

    public ParameterModule getParameterModule() {
        return parameterModule;
    }
}
