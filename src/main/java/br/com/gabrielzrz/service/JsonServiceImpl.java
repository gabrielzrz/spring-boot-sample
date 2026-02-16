package br.com.gabrielzrz.service;

import br.com.gabrielzrz.service.contract.JsonService;
import br.com.gabrielzrz.exception.JsonSerializationException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.lang.reflect.Type;

/**
 * @author Zorzi
 */
@Service
public class JsonServiceImpl implements JsonService {

    private static final String SERIALIZE_ERROR = "Failed to serialize object to JSON";
    private static final String DESERIALIZE_ERROR = "Failed to deserialize JSON";
    private static final String PARSE_ERROR = "Failed to parse JSON string";
    private final JsonMapper jsonMapper;

    public JsonServiceImpl(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public String toJson(Object obj) {
        try {
            return jsonMapper.writeValueAsString(obj);
        } catch (Exception exception) {
            throw new JsonSerializationException(SERIALIZE_ERROR, exception);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return jsonMapper.readValue(json, clazz);
        } catch (Exception exception) {
            throw new JsonSerializationException(DESERIALIZE_ERROR, exception);
        }
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        try {
            JavaType javaType = jsonMapper.constructType(type);
            return jsonMapper.readValue(json, javaType);
        } catch (Exception exception) {
            throw new JsonSerializationException(DESERIALIZE_ERROR, exception);
        }
    }

    @Override
    public JsonNode readTree(String json) {
        try {
            return jsonMapper.readTree(json);
        } catch (Exception exception) {
            throw new JsonSerializationException(PARSE_ERROR, exception);
        }
    }
}
