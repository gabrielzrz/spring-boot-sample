package br.com.gabrielzrz.service;

import br.com.gabrielzrz.dto.security.Token;
import br.com.gabrielzrz.security.JwtTokenProvider;
import br.com.gabrielzrz.dto.security.AccountCredentials;
import br.com.gabrielzrz.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * @author Zorzi
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public Token signIn(AccountCredentials accountCredentials) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountCredentials.username(), accountCredentials.password()));
        User loadedUser = getUser(accountCredentials.username());
        return jwtTokenProvider.createToken(loadedUser.getId(), accountCredentials.username(), loadedUser.getRoles());
    }

    public Token refreshToken(String refreshToken) {
        String username = jwtTokenProvider.extractUsername(refreshToken);
        User loadedUser = getUser(username);
        return jwtTokenProvider.createToken(loadedUser.getId(), username, loadedUser.getRoles());
    }

    private @NonNull User getUser(String username) {
        User loadedUser = userService.findByUsername(username);
        if (!loadedUser.isEnabled()) {
            throw new DisabledException("User account is disabled");
        }
        if (!loadedUser.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }
        if (!loadedUser.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        }
        if (!loadedUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired");
        }
        return loadedUser;
    }
}
