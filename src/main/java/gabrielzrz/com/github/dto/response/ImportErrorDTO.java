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
    private String exception;

    // Erros para ler os arquivos
    private Integer rowNumber; // linha do arquivo que deu erro
    private String field; // qual é a coluna
    private String rejectedValue; // valor da celula

    // Erros para quando for salvar
    private String errorSave; // Adicionar alguma informação que consiga ler o save que deu problema

    // Methods
    public ImportErrorDTO() {
    }

    public ImportErrorDTO(String exception, String errorSave) {
        this.exception = exception;
        this.errorSave = errorSave;
    }

    public ImportErrorDTO(Integer rowNumber, String field, String exception, String rejectedValue) {
        this.rowNumber = rowNumber;
        this.field = field;
        this.exception = exception;
        this.rejectedValue = rejectedValue;
    }

    // Getters && Setters
    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getErrorSave() {
        return errorSave;
    }

    public void setErrorSave(String errorSave) {
        this.errorSave = errorSave;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(String rejectedValue) {
        this.rejectedValue = rejectedValue;
    }
}
