package gabrielzrz.com.github.importer.impl;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.dto.response.ImportErrorDTO;
import gabrielzrz.com.github.dto.response.ImportResultDTO;
import gabrielzrz.com.github.enums.FileType;
import gabrielzrz.com.github.annotation.FileImporterFor;
import gabrielzrz.com.github.importer.base.BaseXlsxImporter;
import gabrielzrz.com.github.util.LambdaUtil;
import io.micrometer.common.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Zorzi
 */
@Component
@FileImporterFor(fileType = FileType.XLSX, dtoClass = PersonDTO.class)
public class PersonXlsxImporter extends BaseXlsxImporter<PersonDTO> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected PersonDTO mapRowToDTO(Row row, ImportResultDTO result) {
        try {
            List<String> errors = validateValues(row);
            if (!errors.isEmpty()) {
                result.incrementFailed();
                result.addError(new ImportErrorDTO(
                        row.getRowNum() + 1,
                        "validation",
                        LambdaUtil.mapToString(errors, s -> s , ", "),
                        ""
                ));
                return null;
            }
            return createPersonDTO(row);
        } catch (DateTimeParseException e) {
            result.incrementFailed();
            result.addError(new ImportErrorDTO(
                    row.getRowNum() + 1,
                    "birthDay",
                    "Data inválida: " + e.getMessage(),
                    row.getCell(3).getStringCellValue()
            ));
        } catch (IllegalArgumentException e) {
            result.incrementFailed();
            result.addError(new ImportErrorDTO(
                    row.getRowNum() + 1,
                    "general",
                    "Campo não encontrado: " + e.getMessage(),
                    ""
            ));
        } catch (Exception e) {
            result.incrementFailed();
            result.addError(new ImportErrorDTO(
                    row.getRowNum() + 1,
                    "general",
                    "Erro inesperado: " + e.getMessage(),
                    ""
            ));
        }
        return null;
    }

    private List<String> validateValues(Row row){
        List<String> errors = new ArrayList<>();

        String name = Optional.ofNullable(row.getCell(0)).map(Cell::getStringCellValue).orElse("");
        if (StringUtils.isBlank(name)) {
            errors.add("Nome é obrigatório");
        }

        String address = Optional.ofNullable(row.getCell(1)).map(Cell::getStringCellValue).orElse("");
        if (StringUtils.isBlank(address)) {
            errors.add("Endereço é obrigatório");
        }

        String gender = Optional.ofNullable(row.getCell(2)).map(Cell::getStringCellValue).orElse("");
        if (StringUtils.isBlank(gender)) {
            errors.add("Gênero é obrigatório");
        }

        String birthDayStr = Optional.ofNullable(row.getCell(3)).map(Cell::getStringCellValue).orElse("");
        if (StringUtils.isBlank(birthDayStr)) {
            errors.add("Data de nascimento é obrigatória");
        }
        return errors;
    }

    private PersonDTO createPersonDTO(Row row) {
        PersonDTO person = new PersonDTO();
        person.setName(row.getCell(0).getStringCellValue());
        person.setAddress(row.getCell(1).getStringCellValue());
        person.setGender(row.getCell(2).getStringCellValue());
        person.setBirthDay(LocalDate.parse(row.getCell(3).getStringCellValue(), formatter));
        return person;
    }
}
