package br.com.gabrielzrz.entity;

import br.com.gabrielzrz.entity.base.BaseEntityVersioned;
import br.com.gabrielzrz.enums.PermissionType;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Zorzi
 */
@Entity
@Audited
@Table(name = "permission")
public class Permission extends BaseEntityVersioned implements GrantedAuthority, Serializable {

    @Serial
    private static final long serialVersionUID = 1446382160747117244L;

    @Enumerated(EnumType.STRING)
    @Column(name = "description", nullable = false, length = 30)
    private PermissionType description;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.description.name();
    }

    //Getters && Setters
    public PermissionType getDescription() {
        return description;
    }

    public void setDescription(PermissionType description) {
        this.description = description;
    }

    //Equals && HashCode
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
