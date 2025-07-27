package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.dto.response.ImportResultDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zorzi
 */
public interface FileImportService {

    <T> ImportResultDTO importFile(MultipartFile file, Class<T> dtoClass, FileImportServiceImpl.ImportCallback<T> callback);
}
