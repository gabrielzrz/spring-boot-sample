package gabrielzrz.com.github.controllers;

import gabrielzrz.com.github.Service.FileStorageService;
import gabrielzrz.com.github.dto.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "File", description = "Upload and Download to files")
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    private FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/uploadFile")
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        Optional.ofNullable(multipartFile).orElseThrow(() -> new IllegalArgumentException("File is not null"));
        String fileName = fileStorageService.storeFile(multipartFile);
        //http://localhost:8080/api/file/downloadFile/filename...
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/file/downloadFile/")
                .path(fileName)
                .toUriString();
        return new UploadFileResponseDTO(fileName, fileDownloadUri, multipartFile.getContentType(), multipartFile.getSize());
    }

    @PostMapping("/uploadFiles")
    public List<UploadFileResponseDTO> uploadMultipleFile(@RequestParam("files") MultipartFile[] multipartFiles) {
        Optional.ofNullable(multipartFiles).orElseThrow(() -> new IllegalArgumentException("Files is not null"));
        return Arrays.asList(multipartFiles).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
    }

    @PostMapping("/downloadFile")
    public ResponseEntity<UploadFileResponseDTO> downloadFile(String fileName, HttpServletRequest request) {
        return null;
    }
}
