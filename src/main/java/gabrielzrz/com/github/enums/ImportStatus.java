package gabrielzrz.com.github.enums;

/**
 * @author Zorzi
 */
public enum ImportStatus {

    SUCCESS("Success"),
    PARTIAL_SUCCESS("Partial Sucess"),
    FAILED("Failed");

    // Constructor
    ImportStatus(String description) {
        this.description = description;
    }

    // Variables
    private String description;

    // Getters
    public String getDescription() {
        return description;
    }
}
