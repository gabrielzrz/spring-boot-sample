package gabrielzrz.com.github.file.importer.impl;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zorzi
 */
@Component
public class CsvImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        CSVFormat format = CSVFormat
                .Builder
                .create()
                .setHeader() // tem cabeçalho
                .setSkipHeaderRecord(true) // pule o cabeçalho
                .setIgnoreEmptyLines(true) // ignora linhas vazias
                .setTrim(true) // elimina o espaço vazio nas celulas
                .build();
        Iterable<CSVRecord> records = format.parse(new InputStreamReader(inputStream));
        return parseRecordsToPersonDTOs(records);
    }

    private List<PersonDTO> parseRecordsToPersonDTOs(Iterable<CSVRecord> records) {
        List<PersonDTO> people = new ArrayList<>();
        for (CSVRecord record : records) {
            PersonDTO person = new PersonDTO();
            person.setName(record.get("name"));
            person.setAddress(record.get("address"));
            person.setGender(record.get("gender"));
            person.setBirthDay(LocalDate.parse(record.get("birthDay")));
            people.add(person);
        }
        return people;
    }
}
