package gabrielzrz.com.github.file.importer.impl;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.dto.response.ImportErrorDTO;
import gabrielzrz.com.github.dto.response.ImportResultDTO;
import gabrielzrz.com.github.enums.ImportStatus;
import gabrielzrz.com.github.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zorzi
 */
@Component
public class CsvImporter implements FileImporter {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public List<PersonDTO> importFile(InputStream inputStream, ImportResultDTO result) {
        CSVFormat format = CSVFormat.DEFAULT
                .withFirstRecordAsHeader() // define primeira linha como cabeçalho
                .withIgnoreEmptyLines(true) // ignora linhas vazias
                .withTrim(true); // elimina espaços vazios nas células
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVParser parser = format.parse(reader)) {
            List<CSVRecord> records = parser.getRecords();
            loadResult(result, parser, records);
            return parseRecordsToPersonDTOs(records, result);
        } catch (IOException e) {
            result.setStatus(ImportStatus.FAILED);
            result.setMessage("Erro ao processar arquivo CSV: " + e.getMessage());
            result.addError(new ImportErrorDTO(0, "file", e.getMessage(), ""));
            return null;
        }
    }

    private List<PersonDTO> parseRecordsToPersonDTOs(Iterable<CSVRecord> records, ImportResultDTO result) {
        List<PersonDTO> people = new ArrayList<>();
        for (CSVRecord record : records) {
            try {
                PersonDTO person = new PersonDTO();
                person.setName(record.get("name"));
                person.setAddress(record.get("address"));
                person.setGender(record.get("gender"));
                person.setBirthDay(LocalDate.parse(record.get("birthDay"), formatter));
                people.add(person);
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
        }
        return people;
    }

    private void loadResult(ImportResultDTO result, CSVParser parser, List<CSVRecord> records) {
        result.setFileType("csv");
        result.setColumnHeaders(new ArrayList<>(parser.getHeaderNames()));
        result.setTotalColumns(parser.getHeaderNames().size());
        result.setTotalRecordsProcessed(records.size());
    }
}
