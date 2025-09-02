package gabrielzrz.com.github.dto.discord.embed;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Zorzi
 * @see <a href="https://discord.com/developers/docs/resources/webhook">Discord documentation</a>
 */
public class EmbedDiscordWebhookDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8604747928252568646L;

    private static final int MAX_FIELD_SIZE = 25;
    private static final int MAX_DESCRIPTION_SIZE = 3835;
    private static final int MAX_TITLE_SIZE = 15;

    // Variables
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("color")
    private int color;
    @JsonProperty("url")
    private String url;
    @JsonProperty("timestamp")
    private String currentDate = DateTimeFormatter.ISO_INSTANT.format(Instant.now().truncatedTo(ChronoUnit.MILLIS));
    @JsonProperty("author")
    private AuthorEmbedDiscordWebhookDTO author;
    @JsonProperty("fields")
    private List<FieldEmbedDiscordWebhookDTO> fields;

    // Methods
    public void addField(FieldEmbedDiscordWebhookDTO field) {
        if (Objects.isNull(fields)) {
            fields = new ArrayList<>();
        }
        if (fields.size() == MAX_FIELD_SIZE) {
            return;
        }
        fields.add(field);
    }

    // Getters/Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = StringUtils.truncate(title, MAX_TITLE_SIZE);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = StringUtils.truncate(description, MAX_DESCRIPTION_SIZE);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public AuthorEmbedDiscordWebhookDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEmbedDiscordWebhookDTO author) {
        this.author = author;
    }

    public List<FieldEmbedDiscordWebhookDTO> getFields() {
        return fields;
    }

    public void setFields(List<FieldEmbedDiscordWebhookDTO> fields) {
        this.fields = fields;
    }
}
