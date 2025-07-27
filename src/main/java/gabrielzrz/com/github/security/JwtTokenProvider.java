package gabrielzrz.com.github.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import gabrielzrz.com.github.dto.security.TokenDTO;
import gabrielzrz.com.github.exception.InvalidJwtAuthenticationException;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Zorzi
 */
@Service
public class JwtTokenProvider {

    private UserDetailsService userDetailsService;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Value("${security.jwt.token.secret-key}")
    private String secretyKey = "secret";

    private long validityInMilliseconds = TimeUnit.HOURS.toMillis(24);

    Algorithm algorithm = null;

    @PostConstruct
    protected void init() {
        secretyKey = Base64.getEncoder().encodeToString(secretyKey.getBytes());
        algorithm = Algorithm.HMAC256(secretyKey.getBytes());
    }

    public TokenDTO refreshToken(String refreshToken) {
        var token = "";
        if(refreshTokenContainsBearer(refreshToken)) {
            token = refreshToken.substring("Bearer ".length());
        }
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        return createAccessToken(username, roles);
    }

    public TokenDTO createAccessToken(String username, List<String> roles) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plus(Duration.ofMillis(validityInMilliseconds * 3));
        String accessToken = getAccessToken(username, roles, now, validity);
        String refreshToken = getRefreshToken(username, roles, now);
        return new TokenDTO(username, true, now, validity, accessToken, refreshToken);
    }

    public TokenDTO createAcessToken(String userName, List<String> roles) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plus(validityInMilliseconds * 3, ChronoUnit.MILLIS);
        String accessToken = getAccessToken(userName, roles, now, validity);
        String refreshToken = getRefreshToken(userName, roles, now) ;
        return new TokenDTO(userName, true, now, validity, accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (refreshTokenContainsBearer(bearerToken)) {
            return bearerToken.substring("Bearer ".length());
        } else {
            return null;
        }
    }

    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            if (decodedJWT.getExpiresAt().before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or Invalid JWT Token");
        }
    }

    private String getRefreshToken(String userName, List<String> roles, LocalDateTime now) {
        LocalDateTime aaa = now.plus(validityInMilliseconds * 3, ChronoUnit.MILLIS);
        Date refreshTokenValidity = Date.from(aaa.atZone(ZoneId.systemDefault()).toInstant());
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(refreshTokenValidity)
                .withSubject(userName)
                .sign(algorithm);
    }

    private String getAccessToken(String userName, List<String> roles, LocalDateTime now, LocalDateTime validity) {
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(Date.from(validity.atZone(ZoneId.systemDefault()).toInstant()))
                .withSubject(userName)
                .withIssuer(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString())
                .sign(algorithm);
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretyKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        return verifier.verify(token);
    }

    private static boolean refreshTokenContainsBearer(String refreshToken) {
        return StringUtils.isNotBlank(refreshToken) && refreshToken.startsWith("Bearer ");
    }
}
