package br.com.gabrielzrz.datatransfer.importer.contract;

import br.com.gabrielzrz.dto.response.ImportResultDTO;

import java.io.InputStream;
import java.util.List;

/**
 * @author Zorzi
 */
public interface FileImporter<T> {

    List<T> importFile(InputStream inputStream, ImportResultDTO result);
}
