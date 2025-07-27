package gabrielzrz.com.github.Service.contract;

import gabrielzrz.com.github.Service.FileImportServiceImpl;
import gabrielzrz.com.github.dto.response.ImportResultDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zorzi
 */
public interface FileImportService {

    <T> ImportResultDTO importFile(MultipartFile file, Class<T> dtoClass, FileImportServiceImpl.ImportCallback<T> callback);
}
