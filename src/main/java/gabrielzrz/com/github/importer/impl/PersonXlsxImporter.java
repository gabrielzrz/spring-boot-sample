package gabrielzrz.com.github.importer.impl;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.importer.base.BaseXlsxImporter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Zorzi
 */
@Component
public class PersonXlsxImporter extends BaseXlsxImporter<PersonDTO> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected PersonDTO mapRowToDTO(Row row) {
        PersonDTO person = new PersonDTO();
        person.setName(row.getCell(0).getStringCellValue());
        person.setAddress(row.getCell(1).getStringCellValue());
        person.setGender(row.getCell(2).getStringCellValue());
        person.setBirthDay(LocalDate.parse(row.getCell(3).getStringCellValue(), formatter));
        return person;
    }
}
