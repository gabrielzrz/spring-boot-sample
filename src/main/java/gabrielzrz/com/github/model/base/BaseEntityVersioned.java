package gabrielzrz.com.github.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import org.hibernate.envers.Audited;

/**
 * @author Zorzi
 */
@MappedSuperclass
public class BaseEntityVersioned extends BaseEntityAuditable {

    @Audited
    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    //Getters && Setters
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
