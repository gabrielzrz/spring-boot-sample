package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.dto.security.AccountCredentialsDTO;
import gabrielzrz.com.github.dto.security.TokenDTO;
import org.springframework.http.ResponseEntity;

/**
 * @author Zorzi
 */
public interface AuthService {

    ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO accountCredentialsDTO);

    ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken);

    AccountCredentialsDTO create(AccountCredentialsDTO user);
}
