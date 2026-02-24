package br.com.gabrielzrz.controllers;

import br.com.gabrielzrz.controllers.assembler.PersonModelAssembler;
import br.com.gabrielzrz.dto.request.filters.PersonFilterRequest;
import br.com.gabrielzrz.dto.request.PersonRequestDTO;
import br.com.gabrielzrz.dto.response.PersonResponseDTO;
import br.com.gabrielzrz.service.contract.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Find by ID")
    public ResponseEntity<PersonResponseDTO> findById(@PathVariable("id") UUID id) {
        PersonResponseDTO personResponseDTO = personService.findById(id);
        return ResponseEntity.ok(personResponseDTO);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Create Person")
    public ResponseEntity<PersonResponseDTO> create(@Valid @RequestBody PersonRequestDTO personRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.create(personRequestDTO));
    }

    @PostMapping(value = "/search",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Find all people")
    public ResponseEntity<PagedModel<EntityModel<PersonResponseDTO>>> findAll(@RequestBody PersonFilterRequest personFilterRequest, Pageable pageable) {
        Page<PersonResponseDTO> peoplePage = personService.findAll(personFilterRequest, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(peoplePage, personModelAssembler));
    }

    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Update Person")
    public ResponseEntity<Void> update(@Valid @RequestBody PersonRequestDTO personRequestDTO) {
        personService.update(personRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "delete person")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
