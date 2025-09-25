package br.com.gabrielzrz.service;

import br.com.gabrielzrz.service.contract.JsonService;
import br.com.gabrielzrz.service.contract.HttpClientService;
import br.com.gabrielzrz.service.contract.HttpRequestLogService;
import br.com.gabrielzrz.dto.MultiPartDTO;
import br.com.gabrielzrz.enums.HttpRequestMethod;
import br.com.gabrielzrz.enums.HttpRequestType;
import br.com.gabrielzrz.model.HttpRequestLog;
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

/**
 * @author Zorzi
 */
@Service
public class HttpClientServiceImpl implements HttpClientService {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientServiceImpl.class);

    private final WebClient webClient;
    private final HttpRequestLogService httpRequestLogService;
    private final JsonService jsonService;

    public HttpClientServiceImpl(WebClient webClient, HttpRequestLogService httpRequestLogService, JsonService jsonService) {
        this.webClient = webClient;
        this.httpRequestLogService = httpRequestLogService;
        this.jsonService = jsonService;
    }

    @Override
    public <T, R> R post(String url, MediaType contentType, T requestBody, HttpRequestType httpRequestType, ParameterizedTypeReference<R> responseType, boolean isToSaveLog) {
        HttpRequestLog httpRequestLog = new HttpRequestLog(url, contentType.toString(), jsonService.toJson(requestBody), httpRequestType, HttpRequestMethod.POST);
        String responseBody = sendPostRequest(url, contentType, requestBody, httpRequestLog);
        httpRequestLog.setResponseBody(responseBody);
        saveLog(isToSaveLog, httpRequestLog);
        return parseResponse(httpRequestLog, responseType);
    }

    // Métodos retornam Mono/Flux
    public <T, R> Mono<R> reactivePost(String url, MediaType contentType, T requestBody, HttpRequestType httpRequestType, ParameterizedTypeReference<R> responseType, boolean isToSaveLog) {
        HttpRequestLog httpRequestLog = new HttpRequestLog(url, contentType.toString(), jsonService.toJson(requestBody), httpRequestType, HttpRequestMethod.POST);
        return reactiveSendPostRequest(url, contentType, requestBody, httpRequestLog)
                .doOnNext(responseBody -> {
                    if (isToSaveLog) {
                        httpRequestLog.setResponseBody(responseBody);
                        httpRequestLogService.save(httpRequestLog);
                    }
                })
                .map(responseBody -> parseResponse(httpRequestLog, responseType));
    }

    @Override
    public <T> void postMultipart(String url, List<MultiPartDTO<T>> parts) {
        MultipartBodyBuilder multipartBodyBuilder = getMultipartBodyBuilder(parts);
        sendPostMultipartRequest(url, multipartBodyBuilder);
    }

    private <T> T parseResponse(HttpRequestLog httpRequestLog, ParameterizedTypeReference<T> responseType) {
        return jsonService.fromJson(httpRequestLog.getResponseBody(), responseType.getType());
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

    // Retorna Mono<String>
    private <T> Mono<String> reactiveSendPostRequest(String url, MediaType contentType, T requestBody, HttpRequestLog httpRequestLog) {
        return webClient.post()
                .uri(url)
                .contentType(contentType)
                .bodyValue(requestBody)
                .exchangeToMono(response -> {
                    httpRequestLog.setStatusCode(response.statusCode().value());
                    httpRequestLog.setResponseHeader(response.headers().asHttpHeaders().toString());
                    return response.bodyToMono(String.class);
                });
    }

    private void sendPostMultipartRequest(String url, MultipartBodyBuilder multipartBodyBuilder) {
        webClient.post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchangeToMono(response -> Mono.just(response.statusCode()))
                .doOnError(error -> logger.error("Error to send MultiParte {}", error.getMessage()))
                .block();
    }

    private void saveLog(boolean isToSaveLog, HttpRequestLog httpRequestLog) {
        if (isToSaveLog) {
            httpRequestLogService.save(httpRequestLog);
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
