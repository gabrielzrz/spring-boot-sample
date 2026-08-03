package br.com.gabrielzrz.service;

import br.com.gabrielzrz.entity.HttpRequestLog;
import br.com.gabrielzrz.repository.jpa.HttpRequestLogRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Zorzi
 */
@Service
public class HttpRequestLogService {

    private final HttpRequestLogRepository httpRequestLogRepository;

    public HttpRequestLogService(HttpRequestLogRepository httpRequestLogRepository) {
        this.httpRequestLogRepository = httpRequestLogRepository;
    }

    public void save(HttpRequestLog httpRequestLog) {
        httpRequestLogRepository.findFirstByUrlAndMethodAndTypeAndResponseBodyIsNotNullOrderByCreatedAtDesc(httpRequestLog.getUrl(), httpRequestLog.getMethod(), httpRequestLog.getType())
            .ifPresent(lastHttpRequestLog -> {
                if (Objects.equals(httpRequestLog.getResponseBody(), lastHttpRequestLog.getResponseBody())) {
                    httpRequestLog.setResponseBody(null);
                }
            });
        httpRequestLogRepository.save(httpRequestLog);
    }
}
