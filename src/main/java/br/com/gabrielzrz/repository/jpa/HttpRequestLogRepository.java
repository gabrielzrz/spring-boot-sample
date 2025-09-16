package br.com.gabrielzrz.repository.jpa;

import br.com.gabrielzrz.enums.HttpRequestMethod;
import br.com.gabrielzrz.enums.HttpRequestType;
import br.com.gabrielzrz.model.HttpRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Repository
public interface HttpRequestLogRepository extends JpaRepository<HttpRequestLog, UUID> {

    Optional<HttpRequestLog> findFirstByUrlAndMethodAndTypeAndResponseBodyIsNotNullOrderByCreatedAtDesc(String url, HttpRequestMethod method, HttpRequestType responseBody);
}
