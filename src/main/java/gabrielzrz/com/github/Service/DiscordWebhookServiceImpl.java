package gabrielzrz.com.github.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gabrielzrz.com.github.Service.contract.DiscordWebhookService;
import gabrielzrz.com.github.Service.contract.HttpClientService;
import gabrielzrz.com.github.Service.contract.ParameterService;
import gabrielzrz.com.github.dto.MultiPartDTO;
import gabrielzrz.com.github.dto.discord.AttachmentDiscordWebhookRequestDTO;
import gabrielzrz.com.github.dto.discord.DiscordWebhookRequestDTO;
import gabrielzrz.com.github.enums.EnvironmentType;
import gabrielzrz.com.github.enums.ParameterType;
import gabrielzrz.com.github.model.Parameter;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Objects;

/**
 * @author Zorzi
 */
@Service
public class DiscordWebhookServiceImpl implements DiscordWebhookService {

    private final HttpClientService httpClientService;
    private final ParameterService parameterService;
    private final ObjectMapper objectMapper;

    public DiscordWebhookServiceImpl(HttpClientService httpClientService, ParameterService parameterService, ObjectMapper objectMapper) {
        this.httpClientService = httpClientService;
        this.parameterService = parameterService;
        this.objectMapper = objectMapper;
    }

    private static final int LOG_ATTACHMENT_INDEX = 1;
    private static final int REQUEST_BODY_ATTACHMENT_INDEX = 0;
    private static final String LOG_TXT = "log.txt";
    private static final String REQUEST_BODY_TXT = "request-body.txt";
    private static final String PAYLOAD_JSON = "payload_json";

    @Async
    @Override
    public void send(String content, String requestBody, Exception exception) {
        String url = parameterService.findByParameterType(ParameterType.SYSTEM_DISCORD_WEBHOOK_URL).getValue();
        if (!isSendWebhook(url, requestBody)) {
            return;
        }
        byte[] requestPayload = requestBody.getBytes();
        byte[] stackTraceData = getStackTraceAsString(exception).getBytes();
        DiscordWebhookRequestDTO webhookRequest = getRequestBody(content, requestPayload, stackTraceData);
        send(url, webhookRequest, requestPayload, stackTraceData);
    }

    private void send(String url, DiscordWebhookRequestDTO webhookRequest, byte[] requestPayload, byte[] stackTraceData) {
        httpClientService.postMultipart(
                url,
                List.of(
                        new MultiPartDTO<>(toJson(webhookRequest), PAYLOAD_JSON, MediaType.TEXT_PLAIN),
                        new MultiPartDTO<>(requestPayload, "files[0]", REQUEST_BODY_TXT, MediaType.APPLICATION_OCTET_STREAM),
                        new MultiPartDTO<>(stackTraceData, "files[1]", LOG_TXT, MediaType.APPLICATION_OCTET_STREAM)
                ));
    }

    private DiscordWebhookRequestDTO getRequestBody(String content, byte[] requestPayload, byte[] stackTraceData) {
        DiscordWebhookRequestDTO requestBody = new DiscordWebhookRequestDTO();
        requestBody.setContent(content);
        requestBody.setAttachments(getAttachments(requestPayload, stackTraceData));
        return requestBody;
    }

    private List<AttachmentDiscordWebhookRequestDTO> getAttachments(byte[] requestPayload, byte[] stackTraceData) {
        return List.of(
                new AttachmentDiscordWebhookRequestDTO(
                        REQUEST_BODY_ATTACHMENT_INDEX,
                        REQUEST_BODY_TXT,
                        MediaType.APPLICATION_OCTET_STREAM_VALUE,
                        requestPayload.length),
                new AttachmentDiscordWebhookRequestDTO(
                        LOG_ATTACHMENT_INDEX,
                        LOG_TXT,
                        MediaType.APPLICATION_OCTET_STREAM_VALUE,
                        stackTraceData.length));
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
        }
        return sw.toString();
    }

    private boolean isSendWebhook(String url, String requestBody) {
        EnvironmentType environmentType = parameterService.getEnvironmentType();
        return Objects.nonNull(url) && EnvironmentType.PROD.equals(environmentType) && requestBody != null;
    }

    private <T> String toJson(T json) {
        try {
            if (Objects.isNull(json)) {
                return null;
            }
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Failed to serialize object to JSON", exception);
        }
    }
}
