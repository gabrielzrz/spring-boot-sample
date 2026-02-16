package br.com.gabrielzrz.dto.security;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author Zorzi
 */
public record Token(
        String username,

        Instant created,

        Instant expiration,

        String accessToken,

        String refreshToken
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 228811861294906434L;
}
