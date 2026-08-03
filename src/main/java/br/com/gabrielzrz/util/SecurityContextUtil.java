package br.com.gabrielzrz.util;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.nonNull;

/**
 * @author Zorzi
 */
public class SecurityContextUtil {

    private SecurityContextUtil() {
    }

    public static UUID getLoggedUserId() {
        return getOptionalJwt()
                .map(jwt -> UUID.fromString(jwt.getClaimAsString("uuid")))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Usuário não autenticado"));
    }

    public static String loggedusername() {
        return getOptionalJwt().map(Jwt::getSubject).orElse("");
    }

    private static Optional<Jwt> getOptionalJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (isValidAuthentication(auth)) {
            return Optional.of((Jwt) auth.getPrincipal());
        }
        return Optional.empty();
    }

    private static boolean isValidAuthentication(Authentication auth) {
        return nonNull(auth) && auth.getPrincipal() != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Jwt;
    }
}