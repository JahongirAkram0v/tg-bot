package store.ddxx.tg.botService;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SendService {

    private final Dotenv dotenv = Dotenv.load();
    private final String botToken = dotenv.get("TELEGRAM_BOT_TOKEN");
    private final String baseUrl = dotenv.get("BASE_URL");

    private final RestTemplate restTemplate = new RestTemplate();

    private void sendTextMessage(Long chatId, String text) {

        String url = baseUrl + botToken + "/sendMessage?chat_id=" + chatId + "&text=" + text;
        restTemplate.getForEntity(url, JsonNode.class);
    }

    public void botSendTextMessage(Long chatId, String text) {
        sendTextMessage(chatId, "\uD83E\uDD16:\t\n" + text);
    }

    public void activeUsersSendTextMessage(Long chatId, String text, String name) {
        sendTextMessage(chatId, "\uD83D\uDC64:  " + name + "\t\n\n" + text);
    }
}
