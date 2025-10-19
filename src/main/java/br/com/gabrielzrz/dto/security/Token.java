package br.com.gabrielzrz.dto.security;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Zorzi
 */
public record Token(
        String username,

        LocalDateTime created,

        LocalDateTime expiration,

        String accessToken,

        String refreshToken
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 228811861294906434L;
}
