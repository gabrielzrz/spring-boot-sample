package br.com.gabrielzrz.exception.handler;

import br.com.gabrielzrz.Service.contract.JsonService;
import br.com.gabrielzrz.exception.*;
import br.com.gabrielzrz.Service.contract.DiscordWebhookService;
import br.com.gabrielzrz.util.SecurityContextUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.Optional;

import static java.util.Objects.isNull;


/**
 * @author Zorzi
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private final DiscordWebhookService discordWebhookService;
    private final JsonService jsonService;

    public GlobalExceptionHandler(DiscordWebhookService discordWebhookService, JsonService jsonService) {
        this.discordWebhookService = discordWebhookService;
        this.jsonService = jsonService;
    }

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception exception, HttpServletRequest request) {
        String url = request.getRequestURI();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        logUnexpectedError(exception, url, httpStatus, request);
        ExceptionResponse response = new ExceptionResponse(url, httpStatus, httpStatus.value(), exception.getMessage(), "");
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler({ResourceNotFoundException.class, UserNameNotFoundException.class, FileNotFoundException.class})
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception exception, HttpServletRequest request) {
        String url = request.getRequestURI();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        logUnexpectedError(exception, url, httpStatus, request);
        ExceptionResponse response = new ExceptionResponse(url, httpStatus, httpStatus.value(), exception.getMessage(), "");
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationException(Exception exception, HttpServletRequest request) {
        String url = request.getRequestURI();
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        logUnexpectedError(exception, url, httpStatus, request);
        ExceptionResponse response = new ExceptionResponse(url, httpStatus, httpStatus.value(), exception.getMessage(), "");
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler({RequiredObjectIsNullException.class, BadRequestException.class})
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(Exception exception, HttpServletRequest request) {
        String url = request.getRequestURI();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        logUnexpectedError(exception, url, httpStatus, request);
        ExceptionResponse response = new ExceptionResponse(url, httpStatus, httpStatus.value(), exception.getMessage(), "");
        return ResponseEntity.status(httpStatus).body(response);
    }

    private void logUnexpectedError(Exception exception, String url, HttpStatus httpStatus, HttpServletRequest request) {
        if (log.isErrorEnabled()) {
            String username = SecurityContextUtil.loggedusername();
            String requestBody = getRequestBody(request);
            String message = exception.getMessage();
            String header = getLogHeader(username, httpStatus, exception, message, url);
            log.error("Erro inesperado: {} | Path: {} | Status: {} | Usuário: {} | Exception: {} ",
                    message,
                    url,
                    httpStatus.value(),
                    username,
                    exception.getClass().getName(),
                    exception
            );
            discordWebhookService.send(header, requestBody, exception);
        }
    }

    private String getLogHeader(String username, HttpStatus httpStatus, Exception exception, String message, String url) {
        return new StringBuilder()
                .append("Erro inesperado: ").append(message)
                .append(" | ")
                .append("Path: ").append(url)
                .append(" | ")
                .append("Status: ").append(httpStatus.value())
                .append(" | ")
                .append("Usuário: ").append(username)
                .append(" | ")
                .append("Exception: ").append(exception.getClass().getName())
                .toString();
    }

    private String getRequestBody(HttpServletRequest request) {
        if (isNull(request)) {
            return "{}";
        }
        String requestBody = findWrapper(request)
                .map(wrapper -> new String(wrapper.getContentAsByteArray()))
                .orElse("{}");
        return jsonService.readTree(requestBody).toString();
    }

    private Optional<ContentCachingRequestWrapper> findWrapper(ServletRequest request) {
        while (request instanceof ServletRequestWrapper wrapper) {
            if (wrapper instanceof ContentCachingRequestWrapper cachingWrapper) {
                return Optional.of(cachingWrapper);
            }
            request = wrapper.getRequest();
        }
        return Optional.empty();
    }
}
