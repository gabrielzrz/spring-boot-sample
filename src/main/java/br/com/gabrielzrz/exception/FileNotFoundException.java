package br.com.gabrielzrz.exception;

/**
 * @author Zorzi
 */
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
