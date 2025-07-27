package gabrielzrz.com.github.importer.base;

import gabrielzrz.com.github.dto.response.ImportErrorDTO;
import gabrielzrz.com.github.dto.response.ImportResultDTO;
import gabrielzrz.com.github.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author Zorzi
 */
public abstract class BaseXlsxImporter<T> implements FileImporter<T> {

    protected abstract T mapRowToDTO(Row row);

    @Override
    public List<T> importFile(InputStream inputStream, ImportResultDTO result) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            return parseRowsToPersonDtoList(rowIterator, result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<T> parseRowsToPersonDtoList(Iterator<Row> rowIterator, ImportResultDTO result) {
        List<T> items = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            try {
                if (isRowValid(row)) {
                    T item = mapRowToDTO(row);
                    Optional.ofNullable(item).ifPresent(items::add);
                }
            } catch (DateTimeParseException e) {
                result.incrementFailed();
                result.addError(new ImportErrorDTO(
                        row.getRowNum(),
                        "birthDay",
                        "Data inválida: " + e.getMessage(),
                        row.getCell(3).getStringCellValue()
                ));
            } catch (IllegalArgumentException e) {
                result.incrementFailed();
                result.addError(new ImportErrorDTO(
                        row.getRowNum(),
                        "general",
                        "Campo não encontrado: " + e.getMessage(),
                        ""
                ));
            } catch (Exception e) {
                result.incrementFailed();
                result.addError(new ImportErrorDTO(
                        row.getRowNum(),
                        "general",
                        "Erro inesperado: " + e.getMessage(),
                        ""
                ));
            }
        }
        return items;
    }

    private boolean isRowValid(Row row) {
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}
