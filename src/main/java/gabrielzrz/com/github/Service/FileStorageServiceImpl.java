package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.Service.contract.FileStorageService;
import gabrielzrz.com.github.config.FileStorageConfig;
import gabrielzrz.com.github.exception.FileNotFoundException;
import gabrielzrz.com.github.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * @author Zorzi
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation; // define o local de armazenamento
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    public FileStorageServiceImpl(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().toAbsolutePath().normalize(); // define o caminho para baixar os arquivos
        this.fileStorageLocation = path;
        try {
            logger.info("Creating Directories");
            Files.createDirectories(this.fileStorageLocation); // se o diretorio não existir, vai ser criado
        } catch (Exception ex) {
            logger.error("Could not create the directory where files will be stored!");
            throw new FileStorageException("Could not create the directory where files will be stored!", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // retira no nome do arquivo caracteres invalidos
        try {
            verifyInvalidSequence(fileName); // impede que insira algo assim no nome ../ para impedir q salve em um diretorio para tras
            logger.info("Saving file in Disk");
            Path targetLocation = this.fileStorageLocation.resolve(fileName); // define o local de armazenamento e o nome
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING); // copia de fato
            return fileName;
        } catch (IOException ex) {
            logger.error("Could not store file {}. Please try Again!", fileName);
            throw new FileStorageException("Could not store file " + fileName + ". Please try Again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize(); // define o local de armazenamento e o nome
            Resource resource = new UrlResource(filePath.toUri());
            return Optional.ofNullable(resource).filter(Resource::exists).orElseThrow(() -> new FileNotFoundException("File " + fileName + " not Found"));
        } catch (Exception ex) {
            logger.error("File {} not found", fileName);
            throw new FileNotFoundException("File " + fileName + " not Found", ex);
        }
    }

    private void verifyInvalidSequence(String fileName) {
        if (fileName.contains("..")) {
            logger.error("Sorry! Filename Contains a Invalid path Sequence {}", fileName);
            throw new FileStorageException("Sorry! Filename Contains a Invalid path Sequence " + fileName);
        }
    }
}
