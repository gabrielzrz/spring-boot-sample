package br.com.gabrielzrz.service.contract;

import tools.jackson.databind.JsonNode;

import java.lang.reflect.Type;

/**
 * @author Zorzi
 */
public interface JsonService {

    String toJson(Object obj);

    <T> T fromJson(String json, Class<T> clazz);

    <T> T fromJson(String json, Type type);

    JsonNode readTree(String json);
}
