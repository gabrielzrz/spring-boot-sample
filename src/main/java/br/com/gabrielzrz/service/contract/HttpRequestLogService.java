package br.com.gabrielzrz.service.contract;

import br.com.gabrielzrz.model.HttpRequestLog;

/**
 * @author Zorzi
 */
public interface HttpRequestLogService {

    void save(HttpRequestLog httpRequestLog);
}
