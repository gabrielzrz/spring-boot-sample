package gabrielzrz.com.github.model.base;

import gabrielzrz.com.github.annotation.UuidGeneratorCustom;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * @author Zorzi
 */
@MappedSuperclass
public class BaseEntityId {

    @Id
    @UuidGeneratorCustom
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    //Getters && Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
