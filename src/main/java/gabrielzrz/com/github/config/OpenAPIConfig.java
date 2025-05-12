package gabrielzrz.com.github.config;

import gabrielzrz.com.github.util.Attribute;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                    .title("Spring boot - API Documentation")
                    .version("1.0.0")
                    .description("Spring Boot API")
                    .license(new License().name("Proprietary Software"))
                    .contact(new Contact().email("gabrielzorzi10@hotmail.com"))
            );
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            sortTagsAlphabetically(openApi);
            sortSchemasAlphabetically(openApi);
            sortAttributesAlphabetically(openApi);
        };
    }

    @Bean
    public SpringDocConfigProperties springDocConfigProperties(SpringDocConfigProperties properties) {
        properties.getCache().setDisabled(true);
        properties.setWriterWithDefaultPrettyPrinter(true);
        return properties;
    }

    private void sortTagsAlphabetically(OpenAPI openApi) {
        openApi.getTags().sort(Comparator.comparing(Tag::getName).thenComparing(Tag::getDescription));
    }

    private void sortSchemasAlphabetically(OpenAPI openApi) {
        openApi.getComponents().setSchemas(new TreeMap<>(openApi.getComponents().getSchemas()));
    }

    private void sortAttributesAlphabetically(OpenAPI openApi) {
        openApi.getComponents().getSchemas().forEach((name, schema) -> {
            Map<String, Schema> standardProperties = getStandardProperties(schema);
            Map<String, Schema> finalProperties = getFinalProperties(schema, standardProperties);
            schema.setProperties(finalProperties);
        });
    }

    private Map<String, Schema> getStandardProperties(Schema schema) {
        Map<String, Schema> properties = new TreeMap<>(schema.getProperties());
        Arrays.asList(Attribute.ID, Attribute.CREATED_AT, Attribute.CREATED_BY).forEach(properties.keySet()::remove);
        return properties;
    }

    private Map<String, Schema> getFinalProperties(Schema schema, Map<String, Schema> standardProperties) {
        Map<String, Schema> properties = new LinkedHashMap<>();
        properties.put(Attribute.ID, (Schema) schema.getProperties().get(Attribute.ID));
        properties.put(Attribute.CREATED_AT, (Schema) schema.getProperties().get(Attribute.CREATED_AT));
        properties.put(Attribute.CREATED_BY, (Schema) schema.getProperties().get(Attribute.CREATED_BY));
        properties.putAll(standardProperties);
        return properties;
    }
}
