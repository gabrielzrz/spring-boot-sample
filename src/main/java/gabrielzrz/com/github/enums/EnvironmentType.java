package gabrielzrz.com.github.enums;

/**
 * @author Zorzi
 */
public enum EnvironmentType {

    DEV("Development"),
    STAGING("Staging"),
    PROD("Production");

    // Variable
    private final String name;

    // Constructors
    EnvironmentType(String name) {
        this.name = name;
    }

    // Getters
    public String getName() {
        return name;
    }

    // ToString
    @Override
    public String toString() {
        return this.name();
    }
}
