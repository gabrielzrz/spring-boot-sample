package gabrielzrz.com.github.model;


import gabrielzrz.com.github.model.base.BaseEntityVersioned;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.envers.Audited;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Zorzi
 */
@Entity
@Audited
@Table(name = "person")
public class Person extends BaseEntityVersioned implements Serializable {

    @Serial
    private static final long serialVersionUID = -7819953910276784376L;

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Address cannot be blank")
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank(message = "Gender cannot be blank")
    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "birth_day", nullable = false)
    private LocalDate birthDay;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = Boolean.TRUE;

    //Getters && Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    //Equals && HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
