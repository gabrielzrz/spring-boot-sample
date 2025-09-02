package gabrielzrz.com.github.Service.contract;

import gabrielzrz.com.github.dto.MultiPartDTO;
import gabrielzrz.com.github.enums.HttpRequestType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * @author Zorzi
 */
public interface HttpClientService {

    <T, R> R post(String url, MediaType contentType, T requestBody, HttpRequestType httpRequestType, ParameterizedTypeReference<R> responseType, boolean isToSaveLog);

    <T> void postMultipart(String url, List<MultiPartDTO<T>> parts);
}
