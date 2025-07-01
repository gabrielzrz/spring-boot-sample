package gabrielzrz.com.github.file.importer.impl;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Zorzi
 */
public class XlsxImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {

        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            return parseRowsToPersonDtoList(rowIterator);
        }

    }

    private List<PersonDTO> parseRowsToPersonDtoList(Iterator<Row> rowIterator) {
        List<PersonDTO> people = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isRowValid(row)) {
                PersonDTO person = new PersonDTO();
                people.add(parseRowtoPersonDto(row));
            }
        }
        return people;
    }

    private PersonDTO parseRowtoPersonDto(Row row) {
        PersonDTO person = new PersonDTO();
        person.setName(row.getCell(0).getStringCellValue());
        person.setAddress(row.getCell(1).getStringCellValue());
        person.setGender(row.getCell(2).getStringCellValue());
        person.setBirthDay(LocalDate.parse(row.getCell(3).getStringCellValue()));
        return person;
    }

    private static boolean isRowValid(Row row) {
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}
