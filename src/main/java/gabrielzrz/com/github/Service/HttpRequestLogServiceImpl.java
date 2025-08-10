package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.Service.contract.HttpRequestLogService;
import gabrielzrz.com.github.constants.RepositoryAdapterConstants;
import gabrielzrz.com.github.model.HttpRequestLog;
import gabrielzrz.com.github.repository.port.HttpRequestLogRepositoryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Zorzi
 */
@Service
public class HttpRequestLogServiceImpl implements HttpRequestLogService {

    private final HttpRequestLogRepositoryPort httpRequestLogRepositoryPort;

    public HttpRequestLogServiceImpl(
            @Qualifier(RepositoryAdapterConstants.Jpa.HTTP_REQUEST) HttpRequestLogRepositoryPort httpRequestLogRepositoryPort) {
        this.httpRequestLogRepositoryPort = httpRequestLogRepositoryPort;
    }

    @Override
    public void save(HttpRequestLog httpRequestLog) {
        httpRequestLogRepositoryPort.findFirstByUrlAndMethodAndTypeAndResponseBodyIsNotNullOrderByCreatedAtDesc(httpRequestLog.getUrl(), httpRequestLog.getMethod(), httpRequestLog.getType())
            .ifPresent(lastHttpRequestLog -> {
                if (Objects.equals(httpRequestLog.getResponseBody(), lastHttpRequestLog.getResponseBody())) {
                    httpRequestLog.setResponseBody(null);
                }
            });
        httpRequestLogRepositoryPort.save(httpRequestLog);
    }
}
