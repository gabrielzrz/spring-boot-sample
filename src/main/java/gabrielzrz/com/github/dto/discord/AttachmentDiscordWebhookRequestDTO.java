package gabrielzrz.com.github.dto.discord;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Zorzi
 * @see <a href="https://discord.com/developers/docs/resources/webhook">Discord documentation</a>
 */
public class AttachmentDiscordWebhookRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 109945389693929719L;

    // Variables
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("filename")
    private String filename;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("content_type")
    private String contentType;
    @JsonProperty("url")
    private String url;
    @JsonProperty("proxy_url")
    private String proxyUrl;
    @JsonProperty("waveform")
    private String waveform;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("flags")
    private Integer flags;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("duration_secs")
    private Double durationSecs;
    @JsonProperty("ephemeral")
    private Boolean ephemeral;

    // Constructors
    public AttachmentDiscordWebhookRequestDTO() {
    }

    public AttachmentDiscordWebhookRequestDTO(Integer id, String filename, String contentType, Integer size) {
        this.id = id;
        this.filename = filename;
        this.contentType = contentType;
        this.size = size;
    }

    // Getters/Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Boolean getEphemeral() {
        return ephemeral;
    }

    public void setEphemeral(Boolean ephemeral) {
        this.ephemeral = ephemeral;
    }

    public Double getDurationSecs() {
        return durationSecs;
    }

    public void setDurationSecs(Double durationSecs) {
        this.durationSecs = durationSecs;
    }

    public String getWaveform() {
        return waveform;
    }

    public void setWaveform(String waveform) {
        this.waveform = waveform;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }
}
