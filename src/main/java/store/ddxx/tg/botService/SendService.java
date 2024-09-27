package store.ddxx.tg.botService;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.UserService;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SendService {

    private final Dotenv dotenv = Dotenv.load();
    private final String botToken = dotenv.get("TELEGRAM_BOT_TOKEN");
    private final String baseUrl = dotenv.get("BASE_URL");
    private final UserService userService;

    private final RestTemplate restTemplate = new RestTemplate();

    public void deleteMessage(Message message) {

        String url = baseUrl + botToken + "/deleteMessage?chat_id=" + message.getChatId()
                +"&message_id=" + message.getMessageId();

        try {
            restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendTextMessage(Long chatId, String text) {

        String url = baseUrl + botToken + "/sendMessage";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", text);

        try {
            restTemplate.postForObject(url, requestBody, String.class);
        } catch (RestClientException e) {
            User user = userService.findById(chatId);
            user.setUserState(UserState.ACTIVATE);
            userService.save(user);
            System.out.println(e.getMessage());
        }
    }

    public void botSendTextMessage(Long chatId, String text) {
        sendTextMessage(chatId, "\uD83E\uDD16:\t\n" + text);
    }

    public void activeUsersSendTextMessage(Long chatId, String text, String name) {
        sendTextMessage(chatId, "\uD83D\uDC64:  " + name + "\t\n\n" + text);
    }

    public void chatReplyKeyboardMarkup(Long chatId, String text) {

        String url = baseUrl + botToken + "/sendMessage";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", "\uD83E\uDD16:\t\n" + text);

        Map<String, Object> keyboardButton = new HashMap<>();
        keyboardButton.put("text", "⚡️ chat");

        List<List<Map<String, Object>>> keyboard = new ArrayList<>();
        keyboard.add(List.of(keyboardButton));

        Map<String, Object> replyMarkup = new HashMap<>();
        replyMarkup.put("keyboard", keyboard);
        replyMarkup.put("resize_keyboard", true);

        requestBody.put("reply_markup", replyMarkup);

        try {
            restTemplate.postForObject(url, requestBody, String.class);
        } catch (RestClientException e) {
            User user = userService.findById(chatId);
            user.setUserState(UserState.ACTIVATE);
            userService.save(user);
            System.out.println(e.getMessage());
        }
    }

    public void controlReplyKeyboardMarkup(Long chatId, String text) {

        String url = baseUrl + botToken + "/sendMessage";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", "\uD83E\uDD16:\t\n" + text);

        Map<String, Object> keyboardButton1 = new HashMap<>();
        keyboardButton1.put("text", "\uD83D\uDED1 stop");

        Map<String, Object> keyboardButton2 = new HashMap<>();
        keyboardButton2.put("text", "➡️ next");

        List<List<Map<String, Object>>> keyboard = new ArrayList<>();
        keyboard.add(List.of(keyboardButton1, keyboardButton2));

        Map<String, Object> replyMarkup = new HashMap<>();
        replyMarkup.put("keyboard", keyboard);
        replyMarkup.put("resize_keyboard", true);

        requestBody.put("reply_markup", replyMarkup);

        try {
            restTemplate.postForObject(url, requestBody, String.class);
        } catch (RestClientException e) {
            User user = userService.findById(chatId);
            user.setUserState(UserState.ACTIVATE);
            userService.save(user);
            System.out.println(e.getMessage());
        }
    }
}
