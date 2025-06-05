package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.exception.UserNameNotFoundException;
import gabrielzrz.com.github.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Zorzi
 */
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameOptional(username).orElseThrow(() -> new UsernameNotFoundException("Username "+ username +" not found!"));
    }
}
