package br.com.gabrielzrz.exception;

/**
 * @author Zorzi
 */
public class UserNameNotFoundException extends RuntimeException {
    public UserNameNotFoundException(String message) {
        super(message);
    }
}
