package gabrielzrz.com.github.repository.adapter;

import gabrielzrz.com.github.constants.RepositoryAdapterConstants;
import gabrielzrz.com.github.enums.HttpRequestMethod;
import gabrielzrz.com.github.enums.HttpRequestType;
import gabrielzrz.com.github.model.HttpRequestLog;
import gabrielzrz.com.github.repository.jpa.HttpRequestLogRepository;
import gabrielzrz.com.github.repository.port.HttpRequestLogRepositoryPort;
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
