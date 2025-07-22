package gabrielzrz.com.github.dto.response;

import gabrielzrz.com.github.enums.ImportStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zorzi
 */
public class ImportResultDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5354588503844459530L;

    // Contadores principais
    private int totalRecordsProcessed; // Total de registros processados
    private int successfulImports; // Importações bem-sucedidas
    private int failedImports; // Importações que falharam
    private int duplicatesSkipped; // Duplicatas ignoradas
    private int emptyRowsIgnored; // Linhas vazias ignoradas

    // Informações do arquivo
    private String fileName; // Nome do arquivo
    private String fileType; // Tipo da extensão
    private long fileSizeInBytes; // Tamanho do arquivo

    // Tempo de processamento
    private long processingTimeInMillis; // Tempo de processamento
    private LocalDateTime importStartTime; // Horário de início
    private LocalDateTime importEndTime; // Horário de fim

    // Detalhes dos erros
    private List<ImportErrorDTO> errors;
    private List<String> warnings;

    // Status geral
    private ImportStatus status;
    private String message;

    // Constructor
    public ImportResultDTO() {
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.importStartTime = LocalDateTime.now();
    }

    // Methods
    public void incrementSuccessful() {
        this.successfulImports++;
    }

    public void incrementFailed() {
        this.failedImports++;
    }

    public void incrementDuplicates() {
        this.duplicatesSkipped++;
    }

    public void incrementEmptyRows() {
        this.emptyRowsIgnored++;
    }

    public void addError(ImportErrorDTO error) {
        this.errors.add(error);
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    public void finishImport() {
        this.importEndTime = LocalDateTime.now();
        this.processingTimeInMillis = Duration.between(importStartTime, importEndTime).toMillis();

        // Define status baseado nos resultados
        if (failedImports == 0 && errors.isEmpty()) {
            this.status = ImportStatus.SUCCESS;
        } else if (successfulImports > 0) {
            this.status = ImportStatus.PARTIAL_SUCCESS;
        } else {
            this.status = ImportStatus.FAILED;
        }
    }

    // Calcula taxa de sucesso
    public double getSuccessRate() {
        if (totalRecordsProcessed == 0) return 0.0;
        return (double) successfulImports / totalRecordsProcessed * 100;
    }

    // Getters && Setters
    public int getTotalRecordsProcessed() {
        return totalRecordsProcessed;
    }

    public void setTotalRecordsProcessed(int totalRecordsProcessed) {
        this.totalRecordsProcessed = totalRecordsProcessed;
    }

    public int getSuccessfulImports() {
        return successfulImports;
    }

    public void setSuccessfulImports(int successfulImports) {
        this.successfulImports = successfulImports;
    }

    public int getFailedImports() {
        return failedImports;
    }

    public void setFailedImports(int failedImports) {
        this.failedImports = failedImports;
    }

    public int getDuplicatesSkipped() {
        return duplicatesSkipped;
    }

    public void setDuplicatesSkipped(int duplicatesSkipped) {
        this.duplicatesSkipped = duplicatesSkipped;
    }

    public int getEmptyRowsIgnored() {
        return emptyRowsIgnored;
    }

    public void setEmptyRowsIgnored(int emptyRowsIgnored) {
        this.emptyRowsIgnored = emptyRowsIgnored;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSizeInBytes() {
        return fileSizeInBytes;
    }

    public void setFileSizeInBytes(long fileSizeInBytes) {
        this.fileSizeInBytes = fileSizeInBytes;
    }

    public long getProcessingTimeInMillis() {
        return processingTimeInMillis;
    }

    public void setProcessingTimeInMillis(long processingTimeInMillis) {
        this.processingTimeInMillis = processingTimeInMillis;
    }

    public LocalDateTime getImportStartTime() {
        return importStartTime;
    }

    public void setImportStartTime(LocalDateTime importStartTime) {
        this.importStartTime = importStartTime;
    }

    public LocalDateTime getImportEndTime() {
        return importEndTime;
    }

    public void setImportEndTime(LocalDateTime importEndTime) {
        this.importEndTime = importEndTime;
    }

    public List<ImportErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ImportErrorDTO> errors) {
        this.errors = errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public ImportStatus getStatus() {
        return status;
    }

    public void setStatus(ImportStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
