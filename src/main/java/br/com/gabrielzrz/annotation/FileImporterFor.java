package br.com.gabrielzrz.annotation;

import br.com.gabrielzrz.enums.FileType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Zorzi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FileImporterFor {
    FileType fileType();
    Class<?> dtoClass();
}
