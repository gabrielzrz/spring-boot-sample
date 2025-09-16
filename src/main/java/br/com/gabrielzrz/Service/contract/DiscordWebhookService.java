package br.com.gabrielzrz.Service.contract;

/**
 * @author Zorzi
 */
public interface DiscordWebhookService {

    void send(String content, String requestBody, Exception exception);
}
