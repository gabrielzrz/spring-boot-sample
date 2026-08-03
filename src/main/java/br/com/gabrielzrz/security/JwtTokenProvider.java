package br.com.gabrielzrz.security;

import br.com.gabrielzrz.dto.security.Token;
import br.com.gabrielzrz.exception.InvalidJwtAuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Service
public class JwtTokenProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.access-token.validity-minutes}")
    private long accessTokenValidityMinutes;

    @Value("${security.jwt.refresh-token.validity-days}")
    private long refreshTokenValidityDays;

    public JwtTokenProvider(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public Token createToken(UUID userId, String username, List<String> roles) {
        Instant now = Instant.now();
        Instant accessValidity = now.plus(accessTokenValidityMinutes, ChronoUnit.MINUTES);
        Instant refreshValidity = now.plus(refreshTokenValidityDays, ChronoUnit.DAYS);
        String accessToken = buildToken(userId, username, roles, now, accessValidity);
        String refreshToken = buildToken(userId, username, roles, now, refreshValidity);
        return new Token(username, now, accessValidity, accessToken, refreshToken);
    }

    public String extractUsername(String token) {
        try {
            return jwtDecoder.decode(token).getSubject();
        } catch (JwtException exception) {
            throw new InvalidJwtAuthenticationException("Expired or Invalid JWT Token", exception);
        }
    }

    private String buildToken(UUID userId, String username, List<String> roles, Instant issuedAt, Instant expiresAt) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(username)
                .claim("uuid", userId.toString())
                .claim("roles", roles)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}