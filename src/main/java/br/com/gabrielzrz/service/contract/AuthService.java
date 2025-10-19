package br.com.gabrielzrz.service.contract;

import br.com.gabrielzrz.dto.security.AccountCredentials;
import br.com.gabrielzrz.dto.security.Token;

/**
 * @author Zorzi
 */
public interface AuthService {

    Token signIn(AccountCredentials accountCredentials);

    Token refreshToken(String username, String refreshToken);
}
