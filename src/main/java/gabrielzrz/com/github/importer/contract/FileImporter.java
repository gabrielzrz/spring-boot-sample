package gabrielzrz.com.github.importer.contract;

import gabrielzrz.com.github.dto.response.ImportResultDTO;

import java.io.InputStream;
import java.util.List;

/**
 * @author Zorzi
 */
public interface FileImporter<T> {

    List<T> importFile(InputStream inputStream, ImportResultDTO result);
}
