package br.com.gabrielzrz.mapper;

import br.com.gabrielzrz.config.MapStructConfig;
import br.com.gabrielzrz.dto.request.PersonRequestDTO;
import br.com.gabrielzrz.dto.response.PersonResponseDTO;
import br.com.gabrielzrz.model.Person;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Zorzi
 */
@Mapper(config = MapStructConfig.class)
public interface PersonMapper {

    // Return Response
    PersonResponseDTO toResponse(Person person);
    Set<PersonResponseDTO> toResponse(Set<Person> people);
    HashSet<PersonResponseDTO> toResponse(HashSet<Person> people);
    TreeSet<PersonResponseDTO> toResponse(TreeSet<Person> people);

    // Return Entity
    Person toEntity(PersonRequestDTO personRequestDTO);
    List<Person> toEntity(List<PersonRequestDTO> personRequestDTO);
    Set<Person> toEntity(Set<PersonRequestDTO> personDTO);
    HashSet<Person> toEntity(HashSet<PersonRequestDTO> personDTO);
    TreeSet<Person> toEntity(TreeSet<PersonRequestDTO> personDTO);
}
