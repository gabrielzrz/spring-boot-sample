package br.com.gabrielzrz.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;

import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * @author Zorzi
 */
@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer customize() {
        return builder -> builder
                .defaultTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC))
                // Remove campos null do JSON ao serializar
                .changeDefaultPropertyInclusion(inclusion ->
                        JsonInclude.Value.construct(
                                JsonInclude.Include.NON_EMPTY,  // Para valores
                                JsonInclude.Include.NON_EMPTY   // Para conteúdo (coleções, arrays)
                        )
                )
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) // Trata strings vazias "" como null ao deserializar
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS) // Permite serializar objetos vazios (sem campos ou todos privados)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // Ignora propriedades no JSON que não existem na classe do Java

    }
}
