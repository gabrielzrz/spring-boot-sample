package br.com.gabrielzrz.service;

import br.com.gabrielzrz.service.contract.AuthService;
import br.com.gabrielzrz.exception.RequiredObjectIsNullException;
import br.com.gabrielzrz.security.JwtTokenProvider;
import br.com.gabrielzrz.dto.security.AccountCredentialsDTO;
import br.com.gabrielzrz.dto.security.TokenDTO;
import br.com.gabrielzrz.exception.UserNameNotFoundException;
import br.com.gabrielzrz.model.User;
import br.com.gabrielzrz.repository.jpa.UserJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserJpaRepository userJpaRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserJpaRepository userJpaRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public TokenDTO signIn(AccountCredentialsDTO accountCredentialsDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountCredentialsDTO.getUsername(), accountCredentialsDTO.getPassword()));
        User user = userJpaRepository.findByUsername(accountCredentialsDTO.getUsername());
        if (user == null) {
            throw new UserNameNotFoundException("Usarname " + accountCredentialsDTO.getUsername() + " not found");
        }
        return jwtTokenProvider.createAcessToken(accountCredentialsDTO.getUsername(), user.getRoles());
    }

    @Override
    public TokenDTO refreshToken(String username, String refreshToken) {
        User user = userJpaRepository.findByUsername(username);
        TokenDTO token;
        if (user != null) {
            token = jwtTokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return token;
    }

    @Override
    public AccountCredentialsDTO create(AccountCredentialsDTO user) {
        if (user == null) {
            throw new RequiredObjectIsNullException();
        }
        logger.info("Creating one new User!");
        User entity = new User();
        entity.setUserName(user.getUsername());
        entity.setFullName(user.getFullname());
        entity.setPassword(generateHashedPassword(user.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);
        User dto = userJpaRepository.save(entity);
        return new AccountCredentialsDTO(dto.getUsername(), dto.getPassword(), dto.getFullName());
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
