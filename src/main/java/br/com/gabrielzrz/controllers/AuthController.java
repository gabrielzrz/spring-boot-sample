package br.com.gabrielzrz.controllers;

import br.com.gabrielzrz.dto.request.UserRequestDTO;
import br.com.gabrielzrz.dto.response.UserResponseDTO;
import br.com.gabrielzrz.dto.security.AccessTokenResponse;
import br.com.gabrielzrz.dto.security.AccountCredentials;
import br.com.gabrielzrz.dto.security.Token;
import br.com.gabrielzrz.service.AuthService;
import br.com.gabrielzrz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

/**
 * @author Zorzi
 */
@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "TOKEN_SPRING_BOOT_SAMPLE";

    private final AuthService authService;
    private final UserService userService;

    @Value("${security.jwt.refresh-token.validity-days}")
    private long refreshTokenValidityDays;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Operation(summary = "Authenticates an user and returns an access token")
    @PostMapping(value = "/signin",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<AccessTokenResponse> signing(@Valid @RequestBody AccountCredentials accountCredentialsDTO) {
        Token token = authService.signIn(accountCredentialsDTO);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshTokenCookie(token.refreshToken()).toString())
                .body(AccessTokenResponse.from(token));
    }

    @Operation(summary = "Exchanges a valid refresh token cookie for a new access token")
    @PostMapping(value = "/refresh", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<AccessTokenResponse> refresh(@CookieValue(REFRESH_TOKEN_COOKIE_NAME) String refreshTokenCookieValue) {
        Token token = authService.refreshToken(refreshTokenCookieValue);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshTokenCookie(token.refreshToken()).toString())
                .body(AccessTokenResponse.from(token));
    }

    @Operation(summary = "Creates a new user")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/users",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userRequestDTO));
    }

    private ResponseCookie buildRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(refreshTokenValidityDays))
                .build();
    }
}