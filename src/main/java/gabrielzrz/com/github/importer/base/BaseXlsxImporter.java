package gabrielzrz.com.github.importer.base;

import gabrielzrz.com.github.dto.response.ImportResultDTO;
import gabrielzrz.com.github.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author Zorzi
 */
public abstract class BaseXlsxImporter<T> implements FileImporter<T> {

    protected abstract T mapRowToDTO(Row row, ImportResultDTO result);

    @Override
    public List<T> importFile(InputStream inputStream, ImportResultDTO result) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            result.setTotalRowsProcessed(sheet.getPhysicalNumberOfRows() - 1);
            Iterator<Row> rowIterator = sheet.iterator();
            loadResult(rowIterator, result);
            return parseRowsToPersonDtoList(rowIterator, result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<T> parseRowsToPersonDtoList(Iterator<Row> rowIterator, ImportResultDTO result) {
        List<T> items = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
//            if (isRowValid(row)) {
                T item = mapRowToDTO(row, result);
                Optional.ofNullable(item).ifPresent(items::add);
//            }
        }
        return items;
    }

    private void loadResult(Iterator<Row> rowIterator, ImportResultDTO result) {
        result.setFileType("xlsx");
        if (rowIterator.hasNext()) {
            Row header = rowIterator.next(); // Pega e descarta o header
            int quantidadeColunas = header.getLastCellNum();
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < quantidadeColunas; i++) {
                Cell cell = header.getCell(i);
                String headerName = cell != null ? cell.toString().trim() : "";
                headers.add(headerName);
            }
            result.setColumnHeaders(headers);
            result.setTotalColumns(quantidadeColunas);
        }
    }

    private boolean isRowValid(Row row) {
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}
