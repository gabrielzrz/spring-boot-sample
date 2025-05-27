package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.dto.security.AccountCredentialsDTO;
import gabrielzrz.com.github.dto.security.TokenDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO accountCredentialsDTO);
}
