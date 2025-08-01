package gabrielzrz.com.github.importer.base;

import gabrielzrz.com.github.dto.response.ImportErrorDTO;
import gabrielzrz.com.github.dto.response.ImportResultDTO;
import gabrielzrz.com.github.enums.ImportStatus;
import gabrielzrz.com.github.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Zorzi
 */
public abstract class BaseCSVImporter<T> implements FileImporter<T> {

    protected abstract T mapRecordToDTO(CSVRecord record, ImportResultDTO result);

    @Override
    public List<T> importFile(InputStream inputStream, ImportResultDTO result) {
        CSVFormat format = CSVFormat.DEFAULT
                .withFirstRecordAsHeader() // define primeira linha como cabeçalho
                .withIgnoreEmptyLines(true) // ignora linhas vazias
                .withTrim(true); // elimina espaços vazios nas células
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVParser parser = format.parse(reader)) {
            List<CSVRecord> records = parser.getRecords();
            loadResult(result, parser, records);
            return parseRecords(records, result);
        } catch (IOException e) {
            result.setStatus(ImportStatus.FAILED);
            result.setMessage("Erro ao processar arquivo CSV: " + e.getMessage());
            result.addError(new ImportErrorDTO(0, "file", e.getMessage(), ""));
            return new ArrayList<>();
        }
    }

    private List<T> parseRecords(Iterable<CSVRecord> records, ImportResultDTO result) {
        List<T> items = new ArrayList<>();
        for (CSVRecord record : records) {
            T item = mapRecordToDTO(record, result);
            Optional.ofNullable(item).ifPresent(items::add);
        }
        return items;
    }

    private void loadResult(ImportResultDTO result, CSVParser parser, List<CSVRecord> records) {
        result.setFileType("csv");
        result.setColumnHeaders(new ArrayList<>(parser.getHeaderNames()));
        result.setTotalColumns(parser.getHeaderNames().size());
        result.setTotalRowsProcessed(records.size());
    }
}
