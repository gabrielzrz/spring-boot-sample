package br.com.gabrielzrz.importer.factory;

import br.com.gabrielzrz.enums.FileType;
import br.com.gabrielzrz.exception.BadRequestException;
import br.com.gabrielzrz.annotation.FileImporterFor;
import br.com.gabrielzrz.importer.contract.ImporterKey;
import br.com.gabrielzrz.importer.contract.FileImporter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @SuppressWarnings("unchecked")
    public <T> FileImporter<T> getImporter(String fileName, Class<T> dtoClass) {
        FileType fileType = FileType.fromFileName(fileName);
        ImporterKey key = new ImporterKey(fileType, dtoClass);
        FileImporter<?> importer = importers.get(key);
        if (importer == null) {
            throw new BadRequestException("Importer não encontrado para " + fileType + " com tipo " + dtoClass.getSimpleName());
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
        if (annotation != null) {
            return new ImporterKey(annotation.fileType(), annotation.dtoClass());
        }
        throw new IllegalStateException("Importer deve ter anotação @FileImporterFor");
    }
}
