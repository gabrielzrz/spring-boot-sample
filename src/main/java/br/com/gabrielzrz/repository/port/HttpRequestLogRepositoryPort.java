package br.com.gabrielzrz.repository.port;

import br.com.gabrielzrz.enums.HttpRequestMethod;
import br.com.gabrielzrz.enums.HttpRequestType;
import br.com.gabrielzrz.model.HttpRequestLog;

import java.util.Optional;

/**
 * @author Zorzi
 */
public interface HttpRequestLogRepositoryPort {

    void save(HttpRequestLog httpRequestLog);

    Optional<HttpRequestLog> findFirstByUrlAndMethodAndTypeAndResponseBodyIsNotNullOrderByCreatedAtDesc(String url, HttpRequestMethod method, HttpRequestType responseBody);
}
