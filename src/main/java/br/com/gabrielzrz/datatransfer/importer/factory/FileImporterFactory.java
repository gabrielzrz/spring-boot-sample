package br.com.gabrielzrz.datatransfer.importer.factory;

import br.com.gabrielzrz.enums.FileType;
import br.com.gabrielzrz.annotation.FileImporterFor;
import br.com.gabrielzrz.datatransfer.importer.contract.FileImporter;
import br.com.gabrielzrz.exception.FileImporterException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Zorzi
 */
@Component
public class FileImporterFactory {

    private final Map<ImporterKey, FileImporter<?>> importers;

    public FileImporterFactory(List<FileImporter<?>> allImporters) {
        this.importers = new HashMap<>();
        initializeImporters(allImporters);
    }

    public <T> FileImporter<T> getImporter(String fileName, Class<T> dtoClass) {
        FileType fileType = FileType.fromFileName(fileName);
        ImporterKey key = new ImporterKey(fileType, dtoClass);
        FileImporter<?> importer = importers.get(key);
        if (importer == null) {
            throw new FileImporterException("Importer not found for " + fileType + " with type " + dtoClass.getSimpleName());
        }
        return (FileImporter<T>) importer;
    }

    private void initializeImporters(List<FileImporter<?>> allImporters) {
        for (FileImporter<?> importer : allImporters) {
            ImporterKey key = extractKeyFromImporter(importer);
            importers.put(key, importer);
        }
    }

    private ImporterKey extractKeyFromImporter(FileImporter<?> importer) {
        FileImporterFor annotation = importer.getClass().getAnnotation(FileImporterFor.class);
        return Optional.ofNullable(annotation)
                .map(a -> new ImporterKey(a.fileType(), a.dtoClass()))
                .orElseThrow(() -> new FileImporterException("Importer must have the @FileImporterFor annotation"));

    }
}
