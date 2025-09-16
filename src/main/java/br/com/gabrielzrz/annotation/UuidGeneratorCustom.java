package br.com.gabrielzrz.annotation;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Zorzi
 */
@IdGeneratorType(br.com.gabrielzrz.model.generator.UuidGeneratorCustom.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface UuidGeneratorCustom {
}
