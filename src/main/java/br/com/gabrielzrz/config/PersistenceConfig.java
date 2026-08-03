package br.com.gabrielzrz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.nonNull;

/**
 * @author Zorzi
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories("br.com.gabrielzrz.repository")
public class PersistenceConfig {

    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(this::isValidAuthentication)
                .map(auth -> UUID.fromString(((Jwt) auth.getPrincipal()).getClaimAsString("uuid")));
    }

    private boolean isValidAuthentication(Authentication auth) {
        return nonNull(auth) && auth.isAuthenticated() && auth.getPrincipal() instanceof Jwt;
    }
}
