package com.matthias.dealmonitor.notify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class TelegramNotifier {

    private final String botToken;
    private final String chatId;
    private final RestClient restClient = RestClient.create();

    public TelegramNotifier(@Value("${TELEGRAM_BOT_TOKEN:}") String botToken,
                            @Value("${TELEGRAM_CHAT_ID:}") String chatId) {
        this.botToken = botToken;
        this.chatId = chatId;
    }

    public boolean isEnabled() {
        return botToken != null && !botToken.isBlank() && chatId != null && !chatId.isBlank();
    }

    public void send(String message) {
        if (!isEnabled()) return;

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("chat_id", chatId, "text", message, "disable_web_page_preview", true))
                .retrieve()
                .toBodilessEntity();
    }
}
