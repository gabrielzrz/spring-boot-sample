package gabrielzrz.com.github.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gabrielzrz.com.github.Service.contract.HttpClientService;
import gabrielzrz.com.github.Service.contract.HttpRequestLogService;
import gabrielzrz.com.github.dto.MultiPartDTO;
import gabrielzrz.com.github.enums.HttpRequestMethod;
import gabrielzrz.com.github.enums.HttpRequestType;
import gabrielzrz.com.github.model.HttpRequestLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * @author Zorzi
 */
@Service
public class HttpClientServiceImpl implements HttpClientService {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientServiceImpl.class);

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final HttpRequestLogService httpRequestLogService;

    public HttpClientServiceImpl(WebClient webClient, ObjectMapper objectMapper, HttpRequestLogService httpRequestLogService) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.httpRequestLogService = httpRequestLogService;
    }

    @Override
    public <T, R> R post(String url, MediaType contentType, T requestBody, HttpRequestType httpRequestType, ParameterizedTypeReference<R> responseType, boolean isToSaveLog) {
        HttpRequestLog httpRequestLog = new HttpRequestLog(url, contentType.toString(), getJson(requestBody), httpRequestType, HttpRequestMethod.POST);
        String responseBody = sendPostRequest(url, contentType, requestBody, httpRequestLog);
        httpRequestLog.setResponseBody(responseBody);
        saveLog(isToSaveLog, httpRequestLog);
        return parseResponse(httpRequestLog, responseType);
    }

    @Override
    public <T> void postMultipart(String url, List<MultiPartDTO<T>> parts) {
        MultipartBodyBuilder multipartBodyBuilder = getMultipartBodyBuilder(parts);
        sendPostMultipartRequest(url, multipartBodyBuilder);
    }

    private <T> T parseResponse(HttpRequestLog httpRequestLog, ParameterizedTypeReference<T> responseType) {
        try {
            return objectMapper.readValue(httpRequestLog.getResponseBody(), objectMapper.constructType(responseType.getType()));
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return null;
        }
    }

    private <T> String sendPostRequest(String url, MediaType contentType, T requestBody, HttpRequestLog httpRequestLog) {
        return webClient.post()
                .uri(url)
                .contentType(contentType)
                .bodyValue(requestBody)
                .exchangeToMono(response -> {
                    httpRequestLog.setStatusCode(response.statusCode().value());
                    httpRequestLog.setResponseHeader(response.headers().asHttpHeaders().toString());
                    return response.bodyToMono(String.class);
                })
                .block();
    }

    private void sendPostMultipartRequest(String url, MultipartBodyBuilder multipartBodyBuilder) {
        webClient.post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchangeToMono(response -> Mono.just(response.statusCode()))
                .doOnError(error -> {
                    logger.error("Error to send MultiParte {}", error.getMessage());
                })
                .block();
    }

    private void saveLog(boolean isToSaveLog, HttpRequestLog httpRequestLog) {
        if (isToSaveLog) {
            httpRequestLogService.save(httpRequestLog);
        }
    }

    private <T> String getJson(T json) {
        try {
            if (Objects.isNull(json)) {
                return null;
            }
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException exception) {
            logger.warn("Error getJson: {}", exception.getMessage());
            return null;
        }
    }

    private <T> MultipartBodyBuilder getMultipartBodyBuilder(List<MultiPartDTO<T>> parts) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        parts.forEach(part -> buildPart(builder, part));
        return builder;
    }

    private <T> void buildPart(MultipartBodyBuilder builder, MultiPartDTO<T> part) {
        builder.part(part.getName(), part.getValue(), part.getContentType()).filename(part.getFilename());
    }
}
