package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.Service.contract.FileImportService;
import gabrielzrz.com.github.dto.response.ImportResultDTO;
import gabrielzrz.com.github.enums.ImportStatus;
import gabrielzrz.com.github.exception.BadRequestException;
import gabrielzrz.com.github.importer.contract.FileImporter;
import gabrielzrz.com.github.importer.factory.FileImporterFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Zorzi
 */
@Service
public class FileImportServiceImpl implements FileImportService {

    private final FileImporterFactory fileImporterFactory;

    public FileImportServiceImpl(FileImporterFactory fileImporterFactory) {
        this.fileImporterFactory = fileImporterFactory;
    }

    @FunctionalInterface
    public interface ImportCallback<T> {
        void saveItems(List<T> items, ImportResultDTO result);
    }

    @Override
    public <T> ImportResultDTO importFile(MultipartFile file, Class<T> dtoClass, ImportCallback<T> callback) {
        ImportResultDTO result = new ImportResultDTO();
        result.setImportStartTime(LocalDateTime.now());
        result.setFileSizeInBytes(file.getSize());
        try {
            List<T> items = readFileAndParse(file, result, dtoClass);
            callback.saveItems(items, result);
        } catch (Exception exception) {
            result.setStatus(ImportStatus.FAILED);
            result.setMessage("Falha na importação: " + exception.getMessage());
        } finally {
            result.finishImport();
        }
        return result;
    }

    private <T> List<T> readFileAndParse(MultipartFile file, ImportResultDTO result, Class<T> dtoClass) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            String filename = Optional.ofNullable(file.getOriginalFilename()).orElseThrow(() -> new BadRequestException("File name cannot be null"));
            FileImporter<T> fileImporter = fileImporterFactory.getImporter(filename, dtoClass);
            result.setFileName(filename);
            return fileImporter.importFile(inputStream, result);
        }
    }
}
