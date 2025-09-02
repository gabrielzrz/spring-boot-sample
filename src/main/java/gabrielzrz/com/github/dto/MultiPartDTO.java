package gabrielzrz.com.github.dto;

import org.springframework.http.MediaType;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Zorzi
 */
public class MultiPartDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -6200658996818988199L;

    // Variables
    private final T value;
    private final String name;
    private final String filename;
    private final MediaType contentType;

    // Constructors
    public MultiPartDTO(T value, String name, MediaType contentType) {
        this(value, name, null, contentType);
    }

    public MultiPartDTO(T value, String name, String filename, MediaType contentType) {
        this.value = value;
        this.name = name;
        this.filename = filename;
        this.contentType = contentType;
    }

    // Getters/Setters
    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public String getFilename() {
        return filename;
    }
}
