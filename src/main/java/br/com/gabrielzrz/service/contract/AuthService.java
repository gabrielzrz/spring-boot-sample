package br.com.gabrielzrz.service.contract;

import br.com.gabrielzrz.dto.security.AccountCredentialsDTO;
import br.com.gabrielzrz.dto.security.TokenDTO;

/**
 * @author Zorzi
 */
public interface AuthService {

    TokenDTO signIn(AccountCredentialsDTO accountCredentialsDTO);

    TokenDTO refreshToken(String username, String refreshToken);

    AccountCredentialsDTO create(AccountCredentialsDTO user);
}
