package store.ddxx.tg.botService;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class SendService {

    private final Dotenv dotenv = Dotenv.load();
    private final String botToken = dotenv.get("TELEGRAM_BOT_TOKEN");
    private final String baseUrl = dotenv.get("BASE_URL");

    private final RestTemplate restTemplate = new RestTemplate();
    private final String sendMessageUrl = baseUrl + botToken + "/sendMessage";

    private void sendTextMessage(Long chatId, String text) {


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", text);

        restTemplate.postForObject(sendMessageUrl, requestBody, String.class);
    }

    public void botSendTextMessage(Long chatId, String text) {
        sendTextMessage(chatId, "\uD83E\uDD16:\t\n" + text);
    }
}
