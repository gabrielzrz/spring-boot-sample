package br.com.gabrielzrz.Service.contract;

import br.com.gabrielzrz.model.HttpRequestLog;

/**
 * @author Zorzi
 */
public interface HttpRequestLogService {

    void save(HttpRequestLog httpRequestLog);
}
