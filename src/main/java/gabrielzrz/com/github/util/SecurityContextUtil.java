package gabrielzrz.com.github.util;

import gabrielzrz.com.github.model.User;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static java.util.Objects.nonNull;

/**
 * @author Zorzi
 */
public class SecurityContextUtil {

    private SecurityContextUtil() {
    }

    public static User getLoggedUser() {
        return getOptionalUser().orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Usuário não autenticado"));
    }

    public static String loggedusername() {
        return SecurityContextUtil.getOptionalUser().map(User::getUsername).orElse("");
    }

    private static Optional<User> getOptionalUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (isValidAuthentication(auth)) {
            return Optional.of((User) auth.getPrincipal());
        }
        return Optional.empty();
    }

    private static boolean isValidAuthentication(Authentication auth) {
        return nonNull(auth) && auth.isAuthenticated() && auth.getPrincipal() instanceof User;
    }
}
