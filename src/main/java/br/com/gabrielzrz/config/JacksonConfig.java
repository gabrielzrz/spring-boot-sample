package br.com.gabrielzrz.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.json.JsonMapper;

import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * @author Zorzi
 */
@Configuration
public class JacksonConfig {

//    @Bean
//    @Primary
//    @DependsOn("customize")
//    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
//        return builder.build();
//    }
//
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer customize() {
//        return builder ->
//                builder.timeZone(TimeZone.getTimeZone(ZoneOffset.UTC))
//                        .serializationInclusion(JsonInclude.Include.NON_NULL)  // Remove campos null do JSON ao serializar
//                        .featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) //Trata strings vazias "" como null ao deserializar objetos
//                        .featuresToDisable(
//                                SerializationFeature.FAIL_ON_EMPTY_BEANS, // Permite serializar objetos vazios (sem campos ou todos privados)
//                                SerializationFeature.WRITE_DATES_WITH_ZONE_ID, // Controlava se incluía o ID da zona (ex: America/Sao_Paulo) na serialização
//                                SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE, // Controlava se datas deveriam usar o timezone do contexto
//                                SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, // To write in milliseconds
//                                DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, // To read in milliseconds
//                                DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, // Controlava se datas deveriam ser ajustadas para o timezone do contexto
//                                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        return JsonMapper.builder()
//                .defaultTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC))
//                .changeDefaultPropertyInclusion(value -> value.withContentInclusion(JsonInclude.Include.NON_NULL))
//                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
//                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
//                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//                .build();
//    }

    @Bean
    public JsonMapperBuilderCustomizer customize() {
        return builder -> builder
                .defaultTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC))
                .changeDefaultPropertyInclusion(value -> value.withContentInclusion(JsonInclude.Include.NON_NULL))
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }
}
