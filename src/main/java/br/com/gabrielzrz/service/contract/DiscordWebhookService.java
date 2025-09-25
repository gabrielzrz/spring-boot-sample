package br.com.gabrielzrz.service.contract;

/**
 * @author Zorzi
 */
public interface DiscordWebhookService {

    void send(String content, String requestBody, Exception exception);
}
