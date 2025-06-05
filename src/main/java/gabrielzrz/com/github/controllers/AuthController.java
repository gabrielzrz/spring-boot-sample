package gabrielzrz.com.github.controllers;

import gabrielzrz.com.github.Service.AuthService;
import gabrielzrz.com.github.dto.security.AccountCredentialsDTO;
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

    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //POST
    @Operation(summary = "Authenticates an user and returns a token")
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDTO accountCredentialsDTO) {
        if (credentialIsNotNull(accountCredentialsDTO)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
        var token = authService.signIn(accountCredentialsDTO);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
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
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (parametersAreInvalid(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = authService.refreshToken(username, refreshToken);
        if (token == null) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        return  ResponseEntity.ok().body(token);
    }

    private boolean credentialIsNotNull(AccountCredentialsDTO accountCredentialsDTO) {
        return accountCredentialsDTO == null || StringUtils.isBlank(accountCredentialsDTO.getUsername()) || StringUtils.isBlank(accountCredentialsDTO.getPassword());
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }
}
