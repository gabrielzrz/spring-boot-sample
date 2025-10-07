package br.com.gabrielzrz.controllers;

import br.com.gabrielzrz.service.contract.AuthService;
import br.com.gabrielzrz.dto.security.AccountCredentialsDTO;
import br.com.gabrielzrz.dto.security.TokenDTO;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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

    //POST
    @Operation(summary = "Authenticates an user and returns a token")
    @PostMapping("/signin")
    public ResponseEntity<TokenDTO> signin(@RequestBody AccountCredentialsDTO accountCredentialsDTO) {
        if (credentialIsNotNull(accountCredentialsDTO)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TokenDTO token = authService.signIn(accountCredentialsDTO);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping(value = "/createUser",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public AccountCredentialsDTO create(@RequestBody AccountCredentialsDTO credentials) {
        return authService.create(credentials);
    }

    //PUT
    @PutMapping("/refresh/{username}")
    public ResponseEntity<TokenDTO> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (parametersAreInvalid(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TokenDTO token = authService.refreshToken(username, refreshToken);
        return  ResponseEntity.ok().body(token);
    }

    private boolean credentialIsNotNull(AccountCredentialsDTO accountCredentialsDTO) {
        return accountCredentialsDTO == null || StringUtils.isBlank(accountCredentialsDTO.getUsername()) || StringUtils.isBlank(accountCredentialsDTO.getPassword());
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }
}
