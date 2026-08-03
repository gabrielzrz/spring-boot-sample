package br.com.gabrielzrz.service;

import br.com.gabrielzrz.dto.request.UserRequestDTO;
import br.com.gabrielzrz.dto.response.UserResponseDTO;
import br.com.gabrielzrz.entity.Permission;
import br.com.gabrielzrz.entity.User;
import br.com.gabrielzrz.entity.UserPermission;
import br.com.gabrielzrz.enums.PermissionType;
import br.com.gabrielzrz.exception.BadRequestException;
import br.com.gabrielzrz.mapper.UserMapper;
import br.com.gabrielzrz.repository.jpa.PermissionJpaRepository;
import br.com.gabrielzrz.repository.jpa.UserJpaRepository;
import br.com.gabrielzrz.util.SecurityContextUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * @author Zorzi
 */
@Service
public class UserService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserJpaRepository userJpaRepository, PermissionJpaRepository permissionJpaRepository,
            PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.permissionJpaRepository = permissionJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userJpaRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username "+ username +" not found!"));
    }

    public User getLoggedUser() {
        return userJpaRepository.findById(SecurityContextUtil.getLoggedUserId()).orElseThrow(() -> new UsernameNotFoundException("Logged user not found"));
    }

    public User findByUsername(String username) {
        return userJpaRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
    }

    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        if (userJpaRepository.findByUsername(userRequestDTO.username()).isPresent()) {
            throw new BadRequestException("Username " + userRequestDTO.username() + " is already in use");
        }
        List<Permission> permissions = permissionJpaRepository.findByDescriptionIn(userRequestDTO.permissions());
        validatePermissionsFound(userRequestDTO.permissions(), permissions);

        User user = new User();
        user.setUsername(userRequestDTO.username());
        user.setFullName(userRequestDTO.fullName());
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setUserPermissions(toUserPermissions(user, permissions));

        return userMapper.toResponse(userJpaRepository.save(user));
    }

    private void validatePermissionsFound(Set<PermissionType> requested, List<Permission> found) {
        if (found.size() == requested.size()) {
            return;
        }
        Set<PermissionType> missing = EnumSet.copyOf(requested);
        found.forEach(permission -> missing.remove(permission.getDescription()));
        throw new BadRequestException("Unknown permissions: " + missing);
    }

    private List<UserPermission> toUserPermissions(User user, List<Permission> permissions) {
        return permissions.stream().map(permission -> {
            UserPermission userPermission = new UserPermission();
            userPermission.setUser(user);
            userPermission.setPermission(permission);
            return userPermission;
        }).toList();
    }
}
