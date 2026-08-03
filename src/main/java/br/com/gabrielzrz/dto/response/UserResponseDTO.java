package br.com.gabrielzrz.dto.response;

import br.com.gabrielzrz.enums.PermissionType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * @author Zorzi
 */
public record UserResponseDTO(
        UUID id,
        String username,
        String fullName,
        Boolean enabled,
        Set<PermissionType> permissions
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
