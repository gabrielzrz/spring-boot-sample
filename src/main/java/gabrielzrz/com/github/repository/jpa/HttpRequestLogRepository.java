package gabrielzrz.com.github.repository.jpa;

import gabrielzrz.com.github.enums.HttpRequestMethod;
import gabrielzrz.com.github.enums.HttpRequestType;
import gabrielzrz.com.github.model.HttpRequestLog;
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
