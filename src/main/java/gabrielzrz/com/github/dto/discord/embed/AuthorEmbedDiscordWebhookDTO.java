package gabrielzrz.com.github.dto.discord.embed;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Zorzi
 * @see <a href="https://discord.com/developers/docs/resources/webhook">Discord documentation</a>
 */
public class AuthorEmbedDiscordWebhookDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2110100955243117092L;

    private static final int MAX_NAME_SIZE = 150;

    // Variables
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("icon_url")
    private String iconUrl;

    // Constructors
    public AuthorEmbedDiscordWebhookDTO() {
    }

    public AuthorEmbedDiscordWebhookDTO(String name) {
        this.name = StringUtils.truncate(name, MAX_NAME_SIZE);
    }

    // Getters/Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.truncate(name, MAX_NAME_SIZE);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
