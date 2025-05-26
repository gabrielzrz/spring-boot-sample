package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.exception.UserNameNotFoundException;
import gabrielzrz.com.github.model.User;
import gabrielzrz.com.github.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Zorzi
 */
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUserName(userName).orElseThrow(() -> new UserNameNotFoundException("UserName " + userName + " not found"));
    }
}
