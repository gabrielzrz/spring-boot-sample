package br.com.gabrielzrz.service;

import br.com.gabrielzrz.service.contract.UserService;
import br.com.gabrielzrz.repository.jpa.UserJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Zorzi
 */
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserJpaRepository userJpaRepository;

    public UserServiceImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userJpaRepository.findByUsernameOptional(username).orElseThrow(() -> new UsernameNotFoundException("Username "+ username +" not found!"));
    }
}
