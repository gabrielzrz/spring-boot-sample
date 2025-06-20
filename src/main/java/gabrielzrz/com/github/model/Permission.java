package gabrielzrz.com.github.model;

import gabrielzrz.com.github.model.base.BaseEntityVersioned;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
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

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    //Methods
    public Permission() {
    }

    @Override
    public String getAuthority() {
        return this.description;
    }

    //Getters && Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
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
