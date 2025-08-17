package gabrielzrz.com.github.mapper;

import gabrielzrz.com.github.config.MapStructConfig;
import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.model.Person;
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

    PersonDTO toDTO(Person person);
    Person toEntity(PersonDTO personDTO);

    List<PersonDTO> toDTO(List<Person> people);
    List<Person> toEntity(List<PersonDTO> personDTO);

    Set<PersonDTO> toDTO(Set<Person> people);
    Set<Person> toEntity(Set<PersonDTO> personDTO);

    HashSet<PersonDTO> toDTO(HashSet<Person> people);
    HashSet<Person> toEntity(HashSet<PersonDTO> personDTO);

    TreeSet<PersonDTO> toDTO(TreeSet<Person> people);
    TreeSet<Person> toEntity(TreeSet<PersonDTO> personDTO);
}
