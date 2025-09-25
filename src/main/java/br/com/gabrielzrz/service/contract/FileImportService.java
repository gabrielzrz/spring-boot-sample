package br.com.gabrielzrz.service.contract;

import br.com.gabrielzrz.service.FileImportServiceImpl;
import br.com.gabrielzrz.dto.response.ImportResultDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zorzi
 */
public interface FileImportService {

    <T> ImportResultDTO importFile(MultipartFile file, Class<T> dtoClass, FileImportServiceImpl.ImportCallback<T> callback);
}
