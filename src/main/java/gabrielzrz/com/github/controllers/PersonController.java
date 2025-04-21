package gabrielzrz.com.github.controllers;

import gabrielzrz.com.github.Service.PersonService;
import gabrielzrz.com.github.model.Person;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zorzi
 */
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    //GET
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Person findById(@PathVariable("id") String id) {
        return personService.findById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Person> findAll() {
        return personService.findAll();
    }

    //POST
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Person create(@RequestBody Person person) {
        return personService.create(person);
    }

    //PUT
    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Person udpate(@RequestBody Person person) {
        return personService.update(person);
    }

    //DELETE
    @DeleteMapping ()
    public void delete(@PathVariable String id) {
        personService.delete(id);
    }
}
