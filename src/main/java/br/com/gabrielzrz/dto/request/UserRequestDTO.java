package br.com.gabrielzrz.dto.request;

import br.com.gabrielzrz.enums.PermissionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Zorzi
 */
public record UserRequestDTO(

        @NotBlank(message = "Username is required")
        @Size(max = 255, message = "Username must not exceed 255 characters")
        String username,

        @NotBlank(message = "Full name is required")
        @Size(max = 255, message = "Full name must not exceed 255 characters")
        String fullName,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
        String password,

        @NotEmpty(message = "At least one permission is required")
        Set<PermissionType> permissions
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
