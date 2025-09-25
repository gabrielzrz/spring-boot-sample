package br.com.gabrielzrz.Service;

import br.com.gabrielzrz.Service.contract.JsonService;
import br.com.gabrielzrz.exception.JsonSerializationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * @author Zorzi
 */
@Service
public class JsonServiceImpl implements JsonService {

    private static final String SERIALIZE_ERROR = "Failed to serialize object to JSON";
    private static final String DESERIALIZE_ERROR = "Failed to deserialize JSON";
    private static final String PARSE_ERROR = "Failed to parse JSON string";
    private final ObjectMapper objectMapper;

    public JsonServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception exception) {
            throw new JsonSerializationException(SERIALIZE_ERROR, exception);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception exception) {
            throw new JsonSerializationException(DESERIALIZE_ERROR, exception);
        }
    }

    @Override
    public <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception exception) {
            throw new JsonSerializationException(DESERIALIZE_ERROR, exception);
        }
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        try {
            JavaType javaType = objectMapper.constructType(type);
            return objectMapper.readValue(json, javaType);
        } catch (Exception exception) {
            throw new JsonSerializationException(DESERIALIZE_ERROR, exception);
        }
    }

    @Override
    public JsonNode readTree(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception exception) {
            throw new JsonSerializationException(PARSE_ERROR, exception);
        }
    }
}
