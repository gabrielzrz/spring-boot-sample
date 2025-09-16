package br.com.gabrielzrz.dto.discord;

import com.fasterxml.jackson.annotation.JsonProperty;
import br.com.gabrielzrz.dto.discord.embed.EmbedDiscordWebhookDTO;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Zorzi
 * @see <a href="https://discord.com/developers/docs/resources/webhook">Discord documentation</a>
 */
public class DiscordWebhookRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3408749084308193881L;

    private static final int MAX_EMBED_SIZE = 10;
    private static final int MAX_CONTENT_SIZE = 2000;

    // Variables
    @JsonProperty("content")
    private String content;
    @JsonProperty("embeds")
    private List<EmbedDiscordWebhookDTO> embeds;
    @JsonProperty("attachments")
    private List<AttachmentDiscordWebhookRequestDTO> attachments;

    // General
    public void addEmbed(EmbedDiscordWebhookDTO embed) {
        if (Objects.isNull(embeds)) {
            embeds = new ArrayList<>();
        }
        if (embeds.size() == MAX_EMBED_SIZE) {
            return;
        }
        embeds.add(embed);
    }

    // Getters/Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = StringUtils.truncate(content, MAX_CONTENT_SIZE);
    }

    public List<EmbedDiscordWebhookDTO> getEmbeds() {
        return embeds;
    }

    public void setEmbeds(List<EmbedDiscordWebhookDTO> embeds) {
        this.embeds = embeds;
    }

    public List<AttachmentDiscordWebhookRequestDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDiscordWebhookRequestDTO> attachments) {
        this.attachments = attachments;
    }
}
