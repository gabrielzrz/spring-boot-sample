package gabrielzrz.com.github.dto.response;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Zorzi
 */
public class ImportErrorDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1534963535408916274L;

    // Variables
    private int rowNumber;
    private String field;
    private String errorMessage;
    private String rejectedValue;

    // Methods
    public ImportErrorDTO() {
    }

    public ImportErrorDTO(int rowNumber, String field, String errorMessage, String rejectedValue) {
        this.rowNumber = rowNumber;
        this.field = field;
        this.errorMessage = errorMessage;
        this.rejectedValue = rejectedValue;
    }

    // Getters && Setters
    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(String rejectedValue) {
        this.rejectedValue = rejectedValue;
    }
}
