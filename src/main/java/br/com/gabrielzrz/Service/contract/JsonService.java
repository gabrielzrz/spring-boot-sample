package br.com.gabrielzrz.Service.contract;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.Type;

/**
 * @author Zorzi
 */
public interface JsonService {

    String toJson(Object obj);

    <T> T fromJson(String json, Class<T> clazz);

    <T> T fromJson(String json, TypeReference<T> typeReference);

    <T> T fromJson(String json, Type type);

    JsonNode readTree(String json);
}
