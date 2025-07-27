package gabrielzrz.com.github.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import gabrielzrz.com.github.enums.ImportStatus;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private int totalRowsProcessed; // Total de registros (linhas) do arquivo
    private int successfulImports; // Importações que foram criadas no banco
    private int failedImports; // Importações que falharam tanto na leitura do arquivo quanto no Save
    private int duplicatesSkipped; // Duplicatas ignoradas
    private BigDecimal sucessRate; // Taxa de sucesso

    // Informações do arquivo
    private String fileName; // Nome do arquivo
    private String fileType; // Tipo da extensão
    private long fileSizeInBytes; // Tamanho do arquivo
    private int totalColumns; // quantidade de colunas
    private List<String> columnHeaders; // quais as colunas do arquivo importado

    // Tempo de processamento
    private long processingTimeInMillis; // Tempo de processamento
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime importStartTime; // Horário de início
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime importEndTime; // Horário de fim

    // Detalhes dos erros
    private List<ImportErrorDTO> errors; // Lista de erros que ocorreram
    private List<String> warnings; // Posso usar se a importação já tem um registro importado

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

    public void addError(ImportErrorDTO error) {
        this.errors.add(error);
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    public void finishImport() {
        this.importEndTime = LocalDateTime.now();
        this.processingTimeInMillis = Duration.between(importStartTime, importEndTime).toMillis();
        calculateSuccessRate();
        chooseStatus();
    }

    public void calculateSuccessRate() {
        // Calcula taxa de sucesso
        sucessRate = new BigDecimal(successfulImports)
                .divide(new BigDecimal(totalRowsProcessed == 0 ? 1 : totalRowsProcessed), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
    }

    public void chooseStatus() {
        // Define status baseado nos resultados
        if (failedImports == 0 && errors != null && errors.isEmpty()) {
            this.status = ImportStatus.SUCCESS;
        } else if (successfulImports > 0) {
            this.status = ImportStatus.PARTIAL_SUCCESS;
        } else {
            this.status = ImportStatus.FAILED;
        }
    }

    // Getters && Setters
    public int getTotalRowsProcessed() {
        return totalRowsProcessed;
    }

    public void setTotalRowsProcessed(int totalRowsProcessed) {
        this.totalRowsProcessed = totalRowsProcessed;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BigDecimal getSucessRate() {
        return sucessRate;
    }

    public void setSucessRate(BigDecimal sucessRate) {
        this.sucessRate = sucessRate;
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

    public int getTotalColumns() {
        return totalColumns;
    }

    public void setTotalColumns(int totalColumns) {
        this.totalColumns = totalColumns;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    public void setColumnHeaders(List<String> columnHeaders) {
        this.columnHeaders = columnHeaders;
    }
}
