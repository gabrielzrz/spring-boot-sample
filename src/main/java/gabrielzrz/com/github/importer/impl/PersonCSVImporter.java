package gabrielzrz.com.github.importer.impl;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.dto.response.ImportErrorDTO;
import gabrielzrz.com.github.dto.response.ImportResultDTO;
import gabrielzrz.com.github.importer.base.BaseCSVImporter;
import gabrielzrz.com.github.util.LambdaUtil;
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
public class PersonCSVImporter extends BaseCSVImporter<PersonDTO> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected PersonDTO mapRecordToDTO(CSVRecord record, ImportResultDTO result) {
        try {
            List<String> errors = validateValues(record);
            if (!errors.isEmpty()) {
                result.incrementFailed();
                result.addError(new ImportErrorDTO(
                        (int) record.getRecordNumber(),
                        "validation",
                        LambdaUtil.mapToString(errors, s -> s , ", "),
                        ""
                ));
                return null;
            }

            PersonDTO person = new PersonDTO();
            person.setName(record.get("name"));
            person.setAddress(record.get("address"));
            person.setGender(record.get("gender"));
            person.setBirthDay(LocalDate.parse(record.get("birthDay"), formatter));
            return person;
        } catch (DateTimeParseException e) {
            result.incrementFailed();
            result.addError(new ImportErrorDTO(
                (int) record.getRecordNumber(),
                "birthDay",
                "Data inválida: " + e.getMessage(),
                record.get("birthDay")
        ));
        } catch (IllegalArgumentException e) {
            result.incrementFailed();
            result.addError(new ImportErrorDTO(
                (int) record.getRecordNumber(),
                "general",
                "Campo não encontrado: " + e.getMessage(),
                ""
        ));
        } catch (Exception e) {
            result.incrementFailed();
            result.addError(new ImportErrorDTO(
                (int) record.getRecordNumber(),
                "general",
                "Erro inesperado: " + e.getMessage(),
                record.toString()
        ));
        }
        return null;
    }

    private List<String> validateValues(CSVRecord record) {
        List<String> errors = new ArrayList<>();

        String name = record.get("name");
        if (StringUtils.isBlank(name)) {
            errors.add("Nome é obrigatório");
        }

        String address = record.get("address");
        if (StringUtils.isBlank(address)) {
            errors.add("Endereço é obrigatório");
        }

        String gender = record.get("gender");
        if (StringUtils.isBlank(gender)) {
            errors.add("Gênero é obrigatório");
        }

        String birthDayStr = record.get("birthDay");
        if (StringUtils.isBlank(birthDayStr)) {
            errors.add("Data de nascimento é obrigatória");
        }
        return errors;
    }
}
