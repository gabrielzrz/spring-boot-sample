package gabrielzrz.com.github.exception.handler;

import gabrielzrz.com.github.exception.*;
import gabrielzrz.com.github.util.SecurityContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * @author Zorzi
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception exception, HttpServletRequest request) {
        String url = request.getRequestURI();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        logUnexpectedError(exception, url, httpStatus);
        ExceptionResponse response = new ExceptionResponse(url, httpStatus, httpStatus.value(), exception.getMessage(), "");
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler({ResourceNotFoundException.class, UserNameNotFoundException.class, FileNotFoundException.class})
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception exception, HttpServletRequest request) {
        String url = request.getRequestURI();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        logUnexpectedError(exception, url, httpStatus);
        ExceptionResponse response = new ExceptionResponse(url, httpStatus, httpStatus.value(), exception.getMessage(), "");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationException(Exception exception, HttpServletRequest request) {
        String url = request.getRequestURI();
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        logUnexpectedError(exception, url, httpStatus);
        ExceptionResponse response = new ExceptionResponse(url, httpStatus, httpStatus.value(), exception.getMessage(), "");
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler({RequiredObjectIsNullException.class, BadRequestException.class})
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(Exception exception, HttpServletRequest request) {
        String url = request.getRequestURI();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        logUnexpectedError(exception, url, httpStatus);
        ExceptionResponse response = new ExceptionResponse(url, httpStatus, httpStatus.value(), exception.getMessage(), "");
        return ResponseEntity.status(httpStatus).body(response);
    }

    private void logUnexpectedError(Exception exception, String url, HttpStatus httpStatus) {
        if (log.isErrorEnabled()) {
            log.error("Erro inesperado: {} | Path: {} | Status: {} | Usuário: {} | Exception: {} ",
                    exception.getMessage(),
                    url,
                    httpStatus.value(),
                    SecurityContextUtil.loggedusername(),
                    exception.getClass().getName(),
                    exception
            );
        }
    }
}
