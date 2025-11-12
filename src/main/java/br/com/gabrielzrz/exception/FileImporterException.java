package br.com.gabrielzrz.exception;

/**
 * @author Zorzi
 */
public class FileImporterException extends RuntimeException {
    public FileImporterException(String message) {
        super(message);
    }

    public FileImporterException(String message, Throwable cause) {
        super(message, cause);
    }
}
