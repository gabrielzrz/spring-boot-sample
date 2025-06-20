package gabrielzrz.com.github.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.yaml.MappingJackson2YamlHttpMessageConverter;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Zorzi
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.origin.Patterns:default}")
    private String corsOriginPatterns = "";

    @Override
    public void addCorsMappings(CorsRegistry registry) { // Cors
        var allowedOrigins = corsOriginPatterns.split(",");
        registry.addMapping("/**") // Informo que as regras abaixo serão para todos os endpoint
                .allowedOrigins("*") // Defino qual origem (frontend) pode acessar meus endpoints
                .allowedMethods("*") // Defino que todos os verbos são permitidos. Exem: GET, POST, PUT, DELETE, PATCH, OPTIONS
                .allowedHeaders("*") // Permite todos os cabeçalhos. Como: Authorization, Content-Type
                .allowCredentials(false); // Define se a API permite credenciais cross-origin. Como: Cookies (JSESSIONID, sessões)
    }

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

//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        configurer.addPathPrefix("/api", HandlerTypePredicate.forBasePackage("gabrielzrz.com.github.controllers"));
//    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        converters.add(new MappingJackson2YamlHttpMessageConverter(yamlMapper));
    }
}
