package gabrielzrz.com.github.model;

import gabrielzrz.com.github.enums.HttpRequestMethod;
import gabrielzrz.com.github.enums.HttpRequestType;
import gabrielzrz.com.github.model.base.BaseEntityAuditable;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Zorzi
 */
@Entity
@Audited
@Table(name = "http_request_log")
public class HttpRequestLog extends BaseEntityAuditable implements Serializable {

    @Serial
    private static final long serialVersionUID = 8698049022794038468L;

    @Column(name = "status_code", nullable = false)
    private Integer statusCode;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "request_header", columnDefinition = "TEXT")
    private String requestHeader;

    @Column(name = "response_header", columnDefinition = "TEXT")
    private String responseHeader;

    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private HttpRequestType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", length = 10, nullable = false)
    private HttpRequestMethod method;

    // Constructor
    public HttpRequestLog() {
    }

    // Getters && Setters
    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(String responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public HttpRequestType getType() {
        return type;
    }

    public void setType(HttpRequestType type) {
        this.type = type;
    }

    public HttpRequestMethod getMethod() {
        return method;
    }

    public void setMethod(HttpRequestMethod method) {
        this.method = method;
    }

    //Equals && HashCode
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestLog httpRequestLog = (HttpRequestLog) o;
        return Objects.equals(id, httpRequestLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
