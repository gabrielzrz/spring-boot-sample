package br.com.gabrielzrz.controllers;

import br.com.gabrielzrz.dto.security.AccountCredentials;
import br.com.gabrielzrz.dto.security.Token;
import br.com.gabrielzrz.service.contract.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Authenticates an user and returns a token")
    @PostMapping(value = "/signin",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Token> signing(@Valid @RequestBody AccountCredentials accountCredentialsDTO) {
        Token token = authService.signIn(accountCredentialsDTO);
        return ResponseEntity.ok().body(token);
    }
}
