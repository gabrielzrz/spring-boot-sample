package br.com.gabrielzrz.Service;

import br.com.gabrielzrz.Service.contract.HttpRequestLogService;
import br.com.gabrielzrz.constants.RepositoryAdapterConstants;
import br.com.gabrielzrz.model.HttpRequestLog;
import br.com.gabrielzrz.repository.port.HttpRequestLogRepositoryPort;
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
