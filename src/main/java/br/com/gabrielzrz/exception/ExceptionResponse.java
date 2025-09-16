package br.com.gabrielzrz.exception;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
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

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
