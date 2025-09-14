package gabrielzrz.com.github.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.yaml.MappingJackson2YamlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Zorzi
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false) // ignora o conteúdo via parametro URL, como ?mediaType=json
                .ignoreAcceptHeader(false) //ativa o uso do cabeçalho Accept para decidir o tipo de resposta.
                .useRegisteredExtensionsOnly(false) // se true, só os formatos registrados abaixo seriam permitidos.
                .defaultContentType(MediaType.APPLICATION_JSON) //define o tipo de conteúdo padrão como application/json caso nenhum seja especificado.
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("yml", MediaType.valueOf("application/x-yaml"));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        converters.add(new MappingJackson2YamlHttpMessageConverter(yamlMapper));
    }
}
