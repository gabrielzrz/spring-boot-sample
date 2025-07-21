package gabrielzrz.com.github.file.importer.impl;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zorzi
 */
@Component
public class CsvImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT
                .withFirstRecordAsHeader() // define primeira linha como cabeçalho
                .withIgnoreEmptyLines(true) // ignora linhas vazias
                .withTrim(true); // elimina espaços vazios nas células
        try (InputStreamReader reader = new InputStreamReader(inputStream);
             CSVParser parser = format.parse(reader)) {
            List<CSVRecord> records = parser.getRecords();
            return parseRecordsToPersonDTOs(records);
        }
    }

    private List<PersonDTO> parseRecordsToPersonDTOs(Iterable<CSVRecord> records) {
        List<PersonDTO> people = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (CSVRecord record : records) {
            PersonDTO person = new PersonDTO();
            person.setName(record.get("name"));
            person.setAddress(record.get("address"));
            person.setGender(record.get("gender"));
            person.setBirthDay(LocalDate.parse(record.get("birthDay"), formatter));
            people.add(person);
        }
        return people;
    }
}
