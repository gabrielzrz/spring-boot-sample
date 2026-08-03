package br.com.gabrielzrz.dto.security;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author Zorzi
 */
public record AccessTokenResponse(
        String username,

        Instant created,

        Instant expiration,

        String accessToken
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static AccessTokenResponse from(Token token) {
        return new AccessTokenResponse(token.username(), token.created(), token.expiration(), token.accessToken());
    }
}