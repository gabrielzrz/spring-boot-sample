package gabrielzrz.com.github.enums;

/**
 * @author Zorzi
 */
public enum ImportStatus {

    SUCCESS("Importação concluída com sucesso"),
    PARTIAL_SUCCESS("Importação concluída parcialmente"),
    FAILED("Importação falhou");

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
