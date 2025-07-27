package gabrielzrz.com.github.importer.impl;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.importer.base.BaseCSVImporter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Zorzi
 */
@Component
public class PersonCSVImporter extends BaseCSVImporter<PersonDTO> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected PersonDTO mapRecordToDTO(CSVRecord record) {
        PersonDTO person = new PersonDTO();
        person.setName(record.get("name"));
        person.setAddress(record.get("address"));
        person.setGender(record.get("gender"));
        person.setBirthDay(LocalDate.parse(record.get("birthDay"), formatter));
        return person;
    }
}
