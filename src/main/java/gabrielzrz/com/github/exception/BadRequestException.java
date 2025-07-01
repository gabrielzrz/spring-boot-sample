package gabrielzrz.com.github.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("Unsupported file extension!");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
