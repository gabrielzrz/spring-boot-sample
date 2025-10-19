package br.com.gabrielzrz.controllers;

import br.com.gabrielzrz.dto.security.AccountCredentials;
import br.com.gabrielzrz.dto.security.Token;
import br.com.gabrielzrz.service.contract.AuthService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zorzi
 */
@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //POST
    @Operation(summary = "Authenticates an user and returns a token")
    @PostMapping("/signin")
    public ResponseEntity<Token> signin(@Valid @RequestBody AccountCredentials accountCredentialsDTO) {
        Token token = authService.signIn(accountCredentialsDTO);
        return ResponseEntity.ok().body(token);
    }

    //PUT
    @PutMapping("/refresh/{username}")
    public ResponseEntity<Token> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (parametersAreInvalid(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Token token = authService.refreshToken(username, refreshToken);
        return  ResponseEntity.ok().body(token);
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }
}
