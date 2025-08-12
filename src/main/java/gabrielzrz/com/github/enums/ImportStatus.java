package gabrielzrz.com.github.enums;

/**
 * @author Zorzi
 */
public enum ImportStatus {

    SUCCESS("Importação concluída com sucesso"),
    PARTIAL_SUCCESS("Importação concluída parcialmente"),
    FAILED("Importação falhou");

    // Variables
    private String description;

    // Constructor
    ImportStatus(String description) {
        this.description = description;
    }

    // Getters
    public String getDescription() {
        return description;
    }
}
