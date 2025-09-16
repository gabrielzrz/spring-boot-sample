package br.com.gabrielzrz.enums;

/**
 * @author Zorzi
 */
public enum FileType {

    CSV("csv"),
    XLSX("xlsx");

    private final String extension;

    FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static FileType fromFileName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        for (FileType type : values()) {
            if (type.extension.equals(extension)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de arquivo não suportado: " + extension);
    }
}
