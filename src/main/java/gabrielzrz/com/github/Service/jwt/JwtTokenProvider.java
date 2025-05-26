package gabrielzrz.com.github.Service.jwt;

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
import org.springframework.cglib.core.Local;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    private UserDetailsService userDetailsService;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretyKey = "secret";
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init() {
        secretyKey = Base64.getEncoder().encodeToString(secretyKey.getBytes());
        algorithm = Algorithm.HMAC256(secretyKey.getBytes());
    }

    public TokenDTO createAcessToken(String userName, List<String> roles) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plus(validityInMilliseconds * 3, ChronoUnit.MILLIS);
        String accessToken = getAcessToken(userName, roles, now, validity);
        String refreshToken = getRefreshToken(userName, roles, now) ;
        return new TokenDTO(userName, true, now, validity, accessToken, refreshToken);
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

    private String getAcessToken(String userName, List<String> roles, LocalDateTime now, LocalDateTime validity) {
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(Date.from(validity.atZone(ZoneId.systemDefault()).toInstant()))
                .withSubject(userName)
                .withIssuer(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString())
                .sign(algorithm);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretyKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        return verifier.verify(token);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String bearer = "Bearer ";
        if (StringUtils.isEmpty(bearerToken) && bearerToken.startsWith(bearer)) {
            return bearerToken.substring(bearer.length());
        } else {
            throw new InvalidJwtAuthenticationException("Invalid JWT Token");
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
}
