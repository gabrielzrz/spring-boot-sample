package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.Service.contract.AuthService;
import gabrielzrz.com.github.exception.RequiredObjectIsNullException;
import gabrielzrz.com.github.security.JwtTokenProvider;
import gabrielzrz.com.github.dto.security.AccountCredentialsDTO;
import gabrielzrz.com.github.dto.security.TokenDTO;
import gabrielzrz.com.github.exception.UserNameNotFoundException;
import gabrielzrz.com.github.model.User;
import gabrielzrz.com.github.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
    private final UserRepository userRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO accountCredentialsDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountCredentialsDTO.getUsername(), accountCredentialsDTO.getPassword()));
        User user = userRepository.findByUsername(accountCredentialsDTO.getUsername());
        if (user == null) {
            throw new UserNameNotFoundException("Usarname " + accountCredentialsDTO.getUsername() + " not found");
        }
        var token = jwtTokenProvider.createAcessToken(accountCredentialsDTO.getUsername(), user.getRoles());
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
        var user = userRepository.findByUsername(username);
        TokenDTO token;
        if (user != null) {
            token = jwtTokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return ResponseEntity.ok(token);
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
        User dto = userRepository.save(entity);
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
