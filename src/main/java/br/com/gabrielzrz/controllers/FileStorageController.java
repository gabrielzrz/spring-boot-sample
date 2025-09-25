package br.com.gabrielzrz.controllers;

import br.com.gabrielzrz.service.contract.FileStorageService;
import br.com.gabrielzrz.dto.response.UploadFileResponseDTO;
import br.com.gabrielzrz.util.LambdaUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Tag(name = "File", description = "Upload and Download to files")
@RestController
@RequestMapping("/api/file-storage")
public class FileStorageController {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    private FileStorageService fileStorageService;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/uploadFile")
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        Objects.requireNonNull(multipartFile, "File must not be null");
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
        return LambdaUtil.mapTo(Arrays.asList(multipartFiles), this::uploadFile);
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        if (resource == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found: " + fileName);
        }
        String contentType = null;
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (Exception ex) {
            logger.error("Could not determine file type!", ex);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
