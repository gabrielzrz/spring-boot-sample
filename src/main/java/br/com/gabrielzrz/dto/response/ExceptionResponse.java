package br.com.gabrielzrz.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author Zorzi
 */
public class ExceptionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 6630459347735524571L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant timestamp;
    private String url;
    private HttpStatus httpStatus;
    private int statusCode;
    private String message;
    private String detail;

    public ExceptionResponse(String url, HttpStatus httpStatus, int statusCode, String message, String detail) {
        this.timestamp = Instant.now();
        this.url = url;
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
        this.message = message;
        this.detail = detail;
    }

    //Getters && Setters
    public Instant getTimestamp() {
        return timestamp;
    }

    public String getUrl() {
        return url;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }
}
