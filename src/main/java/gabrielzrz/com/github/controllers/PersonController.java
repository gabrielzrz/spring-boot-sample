package gabrielzrz.com.github.controllers;

import gabrielzrz.com.github.Service.PersonService;
import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.dto.response.ImportResultDTO;
import gabrielzrz.com.github.enums.ImportStatus;
import gabrielzrz.com.github.model.Person;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author Zorzi
 */
@Tag(name = "Person", description = "Operations related to Person")
@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    //GET
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Find by ID")
    public ResponseEntity<PersonDTO> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok().body(personService.findById(id));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Find all people",
            description = "Returns PersonDTO",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))
                            }
                    ),
                    @ApiResponse(description = "Bad Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(personService.findAll(pageable));
    }

    @GetMapping(value = "/findPeopleByName/{name}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Find people by name")
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPersonByName(
            @PathVariable("name") String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(personService.findPersonByName(name, pageable));
    }

    //POST
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Create Person")
    public ResponseEntity<Person> create(@RequestBody PersonDTO person) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.create(person));
    }

    @PostMapping(
            value = "/massCreation",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Massive people creation with upload of XLSX or CSV")
    public ResponseEntity<ImportResultDTO> massCreation(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            ImportResultDTO errorResult = createErrorResult("Arquivo vazio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }
        if (!isValidFileType(file)) {
            ImportResultDTO errorResult = createErrorResult("Tipo de arquivo inválido. Use CSV ou XLSX");
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResult);
        }
        ImportResultDTO result = personService.massCreation(file);
        HttpStatus status = determineHttpStatus(result);
        return ResponseEntity.status(status).body(result);
    }

    //PUT
    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Update Person")
    public ResponseEntity<Void> udpate(@Valid @RequestBody PersonDTO person) {
        personService.update(person);
        return ResponseEntity.noContent().build();
    }

    //DELETE
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "delete person")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private HttpStatus determineHttpStatus(ImportResultDTO result) {
        switch (result.getStatus()) {
            case SUCCESS:
                return HttpStatus.CREATED; // 201
            case PARTIAL_SUCCESS:
                // Alguns registros foram importados, outros falharam
                return HttpStatus.PARTIAL_CONTENT; // 206
            case FAILED:
                // Nenhum registro foi importado ou erro crítico
                return HttpStatus.UNPROCESSABLE_ENTITY; // 422
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR; // 500
        }
    }

    private ImportResultDTO createErrorResult(String message) {
        ImportResultDTO result = new ImportResultDTO();
        result.setStatus(ImportStatus.FAILED);
        result.setMessage(message);
        result.finishImport();
        return result;
    }

    private boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        return filename != null && (filename.endsWith(".csv") || filename.endsWith(".xlsx"));
    }
}
