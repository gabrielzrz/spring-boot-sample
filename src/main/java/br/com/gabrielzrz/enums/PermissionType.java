package br.com.gabrielzrz.enums;

/**
 * @author Zorzi
 */
public enum PermissionType {

    SUPPORT("Support"),
    ADMIN("Administrator"),
    MANAGER("Manager"),
    COMMON_USER("Common User");

    private final String description;

    PermissionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}