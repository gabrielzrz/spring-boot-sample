package br.com.gabrielzrz.service.contract;

import jakarta.validation.ConstraintViolation;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Set;

/**
 * @author Zorzi
 */
public interface ExceptionMessageParserService {

    String parseDataIntegrityError(DataIntegrityViolationException exception);

    String parseValidationErrors(Set<ConstraintViolation<?>> violations);
}
