package br.com.gabrielzrz.importer.contract;

import br.com.gabrielzrz.enums.FileType;

import java.util.Objects;

/**
 * @author Zorzi
 */
public class ImporterKey {

    private final FileType fileType;
    private final Class<?> dtoClass;

    public ImporterKey(FileType fileType, Class<?> dtoClass) {
        this.fileType = fileType;
        this.dtoClass = dtoClass;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ImporterKey that = (ImporterKey) obj;
        return fileType == that.fileType && Objects.equals(dtoClass, that.dtoClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileType, dtoClass);
    }
}
