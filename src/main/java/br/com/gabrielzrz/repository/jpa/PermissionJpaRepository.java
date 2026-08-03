package br.com.gabrielzrz.repository.jpa;

import br.com.gabrielzrz.entity.Permission;
import br.com.gabrielzrz.enums.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Repository
public interface PermissionJpaRepository extends JpaRepository<Permission, UUID> {

    List<Permission> findByDescriptionIn(Collection<PermissionType> descriptions);
}
