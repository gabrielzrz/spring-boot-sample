package br.com.gabrielzrz.service;

import br.com.gabrielzrz.dto.security.Token;
import br.com.gabrielzrz.service.contract.AuthService;
import br.com.gabrielzrz.security.JwtTokenProvider;
import br.com.gabrielzrz.dto.security.AccountCredentials;
import br.com.gabrielzrz.exception.UserNameNotFoundException;
import br.com.gabrielzrz.model.User;
import br.com.gabrielzrz.repository.jpa.UserJpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zorzi
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserJpaRepository userJpaRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserJpaRepository userJpaRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Token signIn(AccountCredentials accountCredentials) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountCredentials.username(), accountCredentials.password()));
        User user = userJpaRepository.findByUsername(accountCredentials.username());
        if (user == null) {
            throw new UserNameNotFoundException("Usarname " + accountCredentials.username() + " not found");
        }
        return jwtTokenProvider.createAcessToken(accountCredentials.username(), user.getRoles());
    }

    @Override
    public Token refreshToken(String username, String refreshToken) {
        User user = userJpaRepository.findByUsername(username);
        Token token;
        if (user != null) {
            token = jwtTokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return token;
    }

    private String generateHashedPassword(String password) {
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder.encode(password);
    }
}
