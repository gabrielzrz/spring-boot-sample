package br.com.gabrielzrz.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Zorzi
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntityAuditable extends BaseEntityId {

    @Audited
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Audited
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Audited
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private UUID createdBy;

    @Audited
    @LastModifiedBy
    @Column(name = "updated_by")
    private UUID updatedBy;

    //Getters && Setters
    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }
}
