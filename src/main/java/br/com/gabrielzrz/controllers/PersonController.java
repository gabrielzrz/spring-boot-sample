package br.com.gabrielzrz.controllers;

import br.com.gabrielzrz.assembler.PersonModelAssembler;
import br.com.gabrielzrz.dto.request.PersonRequestDTO;
import br.com.gabrielzrz.dto.response.PersonResponseDTO;
import br.com.gabrielzrz.service.contract.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Zorzi
 */
@Tag(name = "Person", description = "Operations related to Person")
@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonModelAssembler personModelAssembler;
    private final PagedResourcesAssembler<PersonResponseDTO> pagedAssembler;
    private final PersonService personService;

    public PersonController(
            PersonModelAssembler personModelAssembler,
            @SuppressWarnings("all") PagedResourcesAssembler<PersonResponseDTO> pagedAssembler,
            PersonService personService) {
        this.personModelAssembler = personModelAssembler;
        this.pagedAssembler = pagedAssembler;
        this.personService = personService;
    }

    //GET
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Find by ID")
    public ResponseEntity<EntityModel<PersonResponseDTO>> findById(@PathVariable("id") UUID id) {
        PersonResponseDTO personResponseDTO = personService.findById(id);
        return ResponseEntity.ok(personModelAssembler.toModel(personResponseDTO));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Find all people")
    public ResponseEntity<PagedModel<EntityModel<PersonResponseDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));

        Page<PersonResponseDTO> peoplePage = personService.findAll(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(peoplePage, personModelAssembler));
    }

    @GetMapping(value = "/findPeopleByName/{name}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Find people by name")
    public ResponseEntity<PagedModel<EntityModel<PersonResponseDTO>>> findPersonByName(
            @PathVariable("name") String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        Page<PersonResponseDTO> peoplePage = personService.findPersonByName(name, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(peoplePage, personModelAssembler));
    }

    //POST
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Create Person")
    public ResponseEntity<PersonResponseDTO> create(@Valid @RequestBody PersonRequestDTO personRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.create(personRequestDTO));
    }

    //PUT
    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Update Person")
    public ResponseEntity<Void> update(@Valid @RequestBody PersonRequestDTO personRequestDTO) {
        personService.update(personRequestDTO);
        return ResponseEntity.noContent().build();
    }

    //DELETE
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "delete person")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
