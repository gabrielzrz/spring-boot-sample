package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.config.FileStorageConfig;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zorzi
 */
public interface FileStorageService {

    String storeFile(MultipartFile file);

    Resource loadFileAsResource(String fileName);
}
