package br.com.gabrielzrz.exception;

/**
 * @author Zorzi
 */
public class JsonSerializationException extends RuntimeException {
    public JsonSerializationException(String message) {
        super(message);
    }

    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
