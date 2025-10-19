package br.com.gabrielzrz.dto.response;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Zorzi
 */
public record UploadFileResponse(
        String fileName,

        String fileDownloadUri,

        String fileType,

        long size
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 280305661362695197L;
}
