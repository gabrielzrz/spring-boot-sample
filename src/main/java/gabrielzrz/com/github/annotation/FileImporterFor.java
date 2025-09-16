package gabrielzrz.com.github.annotation;

import gabrielzrz.com.github.enums.FileType;

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
