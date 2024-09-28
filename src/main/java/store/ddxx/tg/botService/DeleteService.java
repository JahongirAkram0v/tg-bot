package store.ddxx.tg.botService;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class DeleteService {

    private final Dotenv dotenv = Dotenv.load();
    private final String botToken = dotenv.get("TELEGRAM_BOT_TOKEN");
    private final String baseUrl = dotenv.get("BASE_URL");
    private final RestTemplate restTemplate = new RestTemplate();

    public void deleteMessage(Message m) {

        String url = baseUrl + botToken + "/deleteMessage?chat_id=" + m.getChatId() +"&message_id=" + m.getMessageId();

        try {
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.out.println("ERROR !!! : " + e.getMessage());
        }
    }
}
