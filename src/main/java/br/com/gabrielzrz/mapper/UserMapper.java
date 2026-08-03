package br.com.gabrielzrz.mapper;

import br.com.gabrielzrz.config.MapStructConfig;
import br.com.gabrielzrz.dto.response.UserResponseDTO;
import br.com.gabrielzrz.entity.Permission;
import br.com.gabrielzrz.entity.User;
import br.com.gabrielzrz.enums.PermissionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Zorzi
 */
@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    @Mapping(target = "permissions", expression = "java(toPermissionTypes(user))")
    UserResponseDTO toResponse(User user);

    default Set<PermissionType> toPermissionTypes(User user) {
        return user.getPermissions().stream().map(Permission::getDescription).collect(Collectors.toSet());
    }
}
