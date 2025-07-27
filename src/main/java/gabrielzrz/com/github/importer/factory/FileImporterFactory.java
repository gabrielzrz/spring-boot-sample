package gabrielzrz.com.github.importer.factory;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.exception.BadRequestException;
import gabrielzrz.com.github.importer.impl.PersonCSVImporter;
import gabrielzrz.com.github.importer.impl.PersonXlsxImporter;
import gabrielzrz.com.github.importer.contract.FileImporter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Zorzi
 */
@Component
public class FileImporterFactory {

    private ApplicationContext context;

    public FileImporterFactory(ApplicationContext context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public <T> FileImporter<T> getImporter(String fileName, Class<T> dtoClass) {
        if (fileName.endsWith(".xlsx")) {
            if (dtoClass == PersonDTO.class) {
                return (FileImporter<T>) context.getBean(PersonXlsxImporter.class);
            }
        } else if (fileName.endsWith(".csv")) {
            if (dtoClass == PersonDTO.class) {
                return (FileImporter<T>) context.getBean(PersonCSVImporter.class);
            }
        }
        throw new BadRequestException("Formato de arquivo não suportado ou tipo de DTO não reconhecido: " + fileName);
    }
}
