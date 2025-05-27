package gabrielzrz.com.github.controllers;

import gabrielzrz.com.github.Service.AuthService;
import gabrielzrz.com.github.dto.security.AccountCredentialsDTO;
import gabrielzrz.com.github.dto.security.TokenDTO;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Authenticates an user and returns a token")
    @PostMapping("/signin")
    public ResponseEntity<?> signin(AccountCredentialsDTO accountCredentialsDTO) {
        if (credentialIsNotNull(accountCredentialsDTO)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
        var token = authService.signIn(accountCredentialsDTO);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
        return ResponseEntity.ok().body(token);
    }

    private boolean credentialIsNotNull(AccountCredentialsDTO accountCredentialsDTO) {
        return accountCredentialsDTO == null || StringUtils.isBlank(accountCredentialsDTO.getUserName()) || StringUtils.isBlank(accountCredentialsDTO.getPassword());
    }
}
