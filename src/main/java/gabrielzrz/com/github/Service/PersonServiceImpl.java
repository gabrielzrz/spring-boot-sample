package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.controllers.PersonController;
import gabrielzrz.com.github.controllers.TestLogController;
import gabrielzrz.com.github.dto.v1.PersonDTO;
import gabrielzrz.com.github.exception.ResourceNotFoundException;
import gabrielzrz.com.github.mapper.ObjectMapper;
import gabrielzrz.com.github.model.Person;
import gabrielzrz.com.github.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Zorzi
 */
@Service
public class PersonServiceImpl implements PersonService {

    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName());
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonDTO findById(Long id){
        logger.info("Find person by ID");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        var personDto = ObjectMapper.parseObject(entity, PersonDTO.class);
        return addHateoasLink(personDto);
    }

    @Override
    public List<PersonDTO> findAll(){
        logger.info("Find all people");
        List<PersonDTO> people = ObjectMapper.parseListObject(personRepository.findAll(), PersonDTO.class);
        people.forEach(this::addHateoasLink);
        return people;
    }

    @Override
    public Person create(PersonDTO person) {
        logger.info("Create person");
        Person p = ObjectMapper.parseObject(person, Person.class);
        return personRepository.save(p);
    }

    @Override
    public PersonDTO update(PersonDTO person) {
        logger.info("update person");
        Person entity = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + person.getId()));
        entity.setName(person.getName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return ObjectMapper.parseObject(personRepository.save(entity), PersonDTO.class);
    }

    @Override
    public void delete(Long id) {
        logger.info("delete person");
        Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        personRepository.delete(entity);
    }

    private PersonDTO addHateoasLink(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).udpate(dto)).withRel("update").withType("PUT"));
        return dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
