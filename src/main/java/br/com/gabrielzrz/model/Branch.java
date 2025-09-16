package br.com.gabrielzrz.model;

import br.com.gabrielzrz.model.base.BaseEntityVersioned;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Zorzi
 */
@Entity
@Audited
@Table(name = "branch")
public class Branch extends BaseEntityVersioned implements Serializable {

    @Serial
    private static final long serialVersionUID = -6536813994788776318L;

    @Column(name = "branch_name", length = 100, nullable = false)
    private String branchName;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "is_headquarters", nullable = false)
    private boolean isHeadquarters;

    // Getters && Setters
    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isHeadquarters() {
        return isHeadquarters;
    }

    public void setHeadquarters(boolean headquarters) {
        isHeadquarters = headquarters;
    }

    // Equals && HashCode
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
