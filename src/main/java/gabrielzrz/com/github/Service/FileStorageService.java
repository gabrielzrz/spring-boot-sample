package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.config.FileStorageConfig;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zorzi
 */
public interface FileStorageService {

    String storeFile(MultipartFile file);
}
