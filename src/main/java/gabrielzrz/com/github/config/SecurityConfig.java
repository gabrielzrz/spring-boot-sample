package gabrielzrz.com.github.config;

import gabrielzrz.com.github.security.JwtTokenFilter;
import gabrielzrz.com.github.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zorzi
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${cors.origin.Patterns:default}")
    private String corsOriginPatterns = "";

    private JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        encoders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter filter = new JwtTokenFilter(jwtTokenProvider);
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                    .requestMatchers(
                            "/auth/signin",
                            "/auth/refresh/**",
                            "/auth/createUser",
                            "/swagger-ui/**",
                            "/v3/api-docs/**"
                    ).permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/users").denyAll()
                )
                .cors(cors -> {})
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final String[] urls = corsOriginPatterns.split(",");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // Permite o envio de cookies/autenticação
        configuration.setAllowedOriginPatterns(Arrays.asList(urls)); // Libera para os dominios listados no aplication.proprierties
        configuration.addAllowedMethod("*"); // Permite todos os verbos HTTP
        configuration.addAllowedHeader("*"); // Permite todos os headers
        configuration.addExposedHeader("Authorization-Response"); // Faz com que esse header específico fique visível para o frontend
        source.registerCorsConfiguration("/**", configuration); // Aplica essa configuração para todas as rotas da API.
        return source;
    }
}
