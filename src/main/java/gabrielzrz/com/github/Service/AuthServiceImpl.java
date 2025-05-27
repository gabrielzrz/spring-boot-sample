package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.Service.jwt.JwtTokenProvider;
import gabrielzrz.com.github.dto.security.AccountCredentialsDTO;
import gabrielzrz.com.github.dto.security.TokenDTO;
import gabrielzrz.com.github.exception.UserNameNotFoundException;
import gabrielzrz.com.github.model.User;
import gabrielzrz.com.github.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO accountCredentialsDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountCredentialsDTO.getUserName(), accountCredentialsDTO.getPassword()));
        User user = userRepository.findByUserNameUser(accountCredentialsDTO.getUserName());
        if (user == null) {
            throw new UserNameNotFoundException("Usarname " + accountCredentialsDTO.getUserName() + " not found");
        }
        var token = jwtTokenProvider.createAcessToken(accountCredentialsDTO.getUserName(), user.getRoles());
        return ResponseEntity.ok(token);
    }
}
