package gabrielzrz.com.github.repository.port;

import gabrielzrz.com.github.enums.HttpRequestMethod;
import gabrielzrz.com.github.enums.HttpRequestType;
import gabrielzrz.com.github.model.HttpRequestLog;

import java.util.Optional;

/**
 * @author Zorzi
 */
public interface HttpRequestLogRepositoryPort {

    void save(HttpRequestLog httpRequestLog);

    Optional<HttpRequestLog> findFirstByUrlAndMethodAndTypeAndResponseBodyIsNotNullOrderByCreatedAtDesc(String url, HttpRequestMethod method, HttpRequestType responseBody);
}
