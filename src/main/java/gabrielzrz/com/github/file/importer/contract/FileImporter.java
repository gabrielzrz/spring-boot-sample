package gabrielzrz.com.github.file.importer.contract;

import gabrielzrz.com.github.dto.PersonDTO;
import gabrielzrz.com.github.dto.response.ImportResultDTO;

import java.io.InputStream;
import java.util.List;

/**
 * @author Zorzi
 */
public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream, ImportResultDTO result);
}
