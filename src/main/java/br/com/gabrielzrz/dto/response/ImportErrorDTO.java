package br.com.gabrielzrz.dto.response;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Zorzi
 */
public class ImportErrorDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1534963535408916274L;

    // Variables
    private String messageError; // Erro do que aconteceu. Pode ser a excetpion ou uma mensagem personalizada.

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
        this.messageError = exception;
        this.errorSave = errorSave;
    }

    public ImportErrorDTO(Integer rowNumber, String field, String exception, String rejectedValue) {
        this.rowNumber = rowNumber;
        this.field = field;
        this.messageError = exception;
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

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
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
