package gabrielzrz.com.github.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.ZoneOffset;
import java.util.TimeZone;



/**
 * @author Zorzi
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customize() {
        // JavaTimeModule implements ISO-8601
        return builder -> {
            builder.timeZone(TimeZone.getTimeZone(ZoneOffset.UTC))
                    .serializationInclusion(JsonInclude.Include.NON_NULL)
                    .featuresToEnable(
                            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, // To write in milliseconds
                            DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                    .featuresToDisable(
                            SerializationFeature.FAIL_ON_EMPTY_BEANS,
                            SerializationFeature.WRITE_DATES_WITH_ZONE_ID,
                            SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE,
                            SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, // To write in milliseconds
                            DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, // To read in milliseconds
                            DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,
                            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        };
    }

    @Bean
    @Primary
    @DependsOn("customize")
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.build();
    }
}
