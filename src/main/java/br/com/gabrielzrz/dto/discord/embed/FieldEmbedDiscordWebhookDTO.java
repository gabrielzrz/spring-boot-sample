package br.com.gabrielzrz.dto.discord.embed;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;

public class FieldEmbedDiscordWebhookDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7344375052422743025L;

    private static final int MAX_NAME_SIZE = 256;
    private static final int MAX_VALUE_SIZE = 1024;

    // Variables
    @JsonProperty("name")
    private String name;
    @JsonProperty("value")
    private String value;

    // Getters/Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.truncate(name, MAX_NAME_SIZE);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = StringUtils.truncate(value, MAX_VALUE_SIZE);
    }
}
