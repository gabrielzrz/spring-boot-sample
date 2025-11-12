package br.com.gabrielzrz.exception;

/**
 * @author Zorzi
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
