package br.com.gabrielzrz.repository.adapter;

import br.com.gabrielzrz.constants.RepositoryAdapterConstants;
import br.com.gabrielzrz.enums.HttpRequestMethod;
import br.com.gabrielzrz.enums.HttpRequestType;
import br.com.gabrielzrz.model.HttpRequestLog;
import br.com.gabrielzrz.repository.jpa.HttpRequestLogRepository;
import br.com.gabrielzrz.repository.port.HttpRequestLogRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Zorzi
 */
@Component(RepositoryAdapterConstants.Jpa.HTTP_REQUEST)
public class HttpRequestLogJpaRepositoryAdapter implements HttpRequestLogRepositoryPort {

    private final HttpRequestLogRepository httpRequestLogRepository;

    public HttpRequestLogJpaRepositoryAdapter(HttpRequestLogRepository httpRequestLogRepository) {
        this.httpRequestLogRepository = httpRequestLogRepository;
    }

    @Override
    public void save(HttpRequestLog httpRequestLog) {
        httpRequestLogRepository.save(httpRequestLog);
    }

    @Override
    public Optional<HttpRequestLog> findFirstByUrlAndMethodAndTypeAndResponseBodyIsNotNullOrderByCreatedAtDesc(String url, HttpRequestMethod method, HttpRequestType responseBody) {
        return httpRequestLogRepository.findFirstByUrlAndMethodAndTypeAndResponseBodyIsNotNullOrderByCreatedAtDesc(url, method, responseBody);
    }
}
