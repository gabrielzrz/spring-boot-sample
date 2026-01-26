package br.com.gabrielzrz.dto.security;

import com.fasterxml.jackson.annotation.JsonInclude;

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

//        @JsonInclude(JsonInclude.Include.NON_NULL)
        String accessToken,

        String refreshToken
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 228811861294906434L;
}
