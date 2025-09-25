package br.com.gabrielzrz.controllers;

import br.com.gabrielzrz.service.contract.PersonService;
import br.com.gabrielzrz.dto.response.ImportResultDTO;
import br.com.gabrielzrz.enums.ImportStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zorzi
 */
@Tag(name = "Data Transfer", description = "Data Import or Export Operations")
@RestController
@RequestMapping("/api/data-transfer")
public class DataTransferController {

    private final PersonService personService;

    public DataTransferController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping(value = "/importPeople",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Operation(summary = "Massive people creation with upload of XLSX or CSV")
    public ResponseEntity<ImportResultDTO> importPeople(@RequestParam("file") MultipartFile file) {
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

    private ImportResultDTO createErrorResult(String message) {
        ImportResultDTO result = new ImportResultDTO();
        result.setStatus(ImportStatus.FAILED);
        result.setMessage(message);
        result.finishImport();
        return result;
    }

    private boolean isValidFileType(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return filename != null && (filename.endsWith(".csv") || filename.endsWith(".xlsx"));
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
}
