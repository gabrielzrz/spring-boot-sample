package br.com.gabrielzrz.dto.response;

import org.springframework.hateoas.server.core.Relation;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Relation(collectionRelation = "people")
public class PersonResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1747815026134514174L;

    private UUID id;

    private String name;

    private String address;

    private String gender;

    private LocalDate birthDay;

    public PersonResponseDTO() {
    }

    public PersonResponseDTO(UUID id, String name, String address, String gender, LocalDate birthDay) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.birthDay = birthDay;
    }

    // Getters && Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
}
