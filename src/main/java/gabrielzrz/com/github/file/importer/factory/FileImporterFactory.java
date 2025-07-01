package gabrielzrz.com.github.file.importer.factory;

import gabrielzrz.com.github.exception.BadRequestException;
import gabrielzrz.com.github.file.importer.contract.FileImporter;
import gabrielzrz.com.github.file.importer.impl.CsvImporter;
import gabrielzrz.com.github.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Zorzi
 */
@Component
public class FileImporterFactory {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());
    private ApplicationContext context;

    public FileImporterFactory(ApplicationContext context) {
        this.context = context;
    }

    public FileImporter getImporter(String fileName) {
        if (fileName.endsWith(".xlsx")) {
            return context.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            return context.getBean(CsvImporter.class);
        } else {
            throw new BadRequestException();
        }
    }
}
