package br.com.gabrielzrz.datatransfer.importer.impl;

import br.com.gabrielzrz.dto.PersonDTO;
import br.com.gabrielzrz.dto.response.ImportErrorDTO;
import br.com.gabrielzrz.dto.response.ImportResultDTO;
import br.com.gabrielzrz.enums.FileType;
import br.com.gabrielzrz.annotation.FileImporterFor;
import br.com.gabrielzrz.datatransfer.importer.base.BaseCSVImporter;
import br.com.gabrielzrz.util.LambdaUtil;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zorzi
 */
@Component
@FileImporterFor(fileType = FileType.CSV, dtoClass = PersonDTO.class)
public class PersonCSVImporter extends BaseCSVImporter<PersonDTO> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected PersonDTO mapRecordToDTO(CSVRecord line, ImportResultDTO result) {
        try {
            List<String> errors = validateValues(line);
            if (!errors.isEmpty()) {
                result.incrementFailed();
                result.addError(new ImportErrorDTO(
                        (int) line.getRecordNumber(),
                        "validation",
                        LambdaUtil.mapToString(errors, s -> s , ", "),
                        ""
                ));
                return null;
            }
            return createPersonDTO(line);
        } catch (DateTimeParseException e) {
            result.incrementFailed();
            result.addError(new ImportErrorDTO(
                (int) line.getRecordNumber(),
                "birthDay",
                "Data inválida: " + e.getMessage(),
                line.get("birthDay")
        ));
        } catch (IllegalArgumentException e) {
            result.incrementFailed();
            result.addError(new ImportErrorDTO(
                (int) line.getRecordNumber(),
                "general",
                "Campo não encontrado: " + e.getMessage(),
                ""
        ));
        } catch (Exception e) {
            result.incrementFailed();
            result.addError(new ImportErrorDTO(
                (int) line.getRecordNumber(),
                "general",
                "Erro inesperado: " + e.getMessage(),
                line.toString()
        ));
        }
        return null;
    }

    private List<String> validateValues(CSVRecord line) {
        List<String> errors = new ArrayList<>();

        String name = line.get("name");
        if (StringUtils.isBlank(name)) {
            errors.add("Nome é obrigatório");
        }

        String address = line.get("address");
        if (StringUtils.isBlank(address)) {
            errors.add("Endereço é obrigatório");
        }

        String gender = line.get("gender");
        if (StringUtils.isBlank(gender)) {
            errors.add("Gênero é obrigatório");
        }

        String birthDayStr = line.get("birthDay");
        if (StringUtils.isBlank(birthDayStr)) {
            errors.add("Data de nascimento é obrigatória");
        }
        return errors;
    }

    private PersonDTO createPersonDTO(CSVRecord line) {
        PersonDTO person = new PersonDTO();
        person.setName(line.get("name"));
        person.setAddress(line.get("address"));
        person.setGender(line.get("gender"));
        person.setBirthDay(LocalDate.parse(line.get("birthDay"), formatter));
        return person;
    }
}
