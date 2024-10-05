package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SendService {

    private final CheckUserService checkUserService;

    public void sendTextMessage(Long chatId, String text) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", text);

        checkUserService.check(requestBody, chatId);
    }

    public void botSendTextMessage(Long chatId, String text) {
        sendTextMessage(chatId, "\uD83E\uDD16:\t\n" + text);
    }

    public void activeUsersSendTextMessage(Long chatId, String text, String name) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", "\uD83D\uDC64:  " + name + "\t\n\n" + text);

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

        checkUserService.check(requestBody, chatId);
    }

    public void chatReplyKeyboardMarkup(Long chatId, String text) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", text);

        Map<String, Object> keyboardButton = new HashMap<>();
        keyboardButton.put("text", "⚡️ chat");

        List<List<Map<String, Object>>> keyboard = new ArrayList<>();
        keyboard.add(List.of(keyboardButton));

        Map<String, Object> replyMarkup = new HashMap<>();
        replyMarkup.put("keyboard", keyboard);
        replyMarkup.put("resize_keyboard", true);

        requestBody.put("reply_markup", replyMarkup);

        checkUserService.check(requestBody, chatId);
    }

    public void controlReplyKeyboardMarkup(Long chatId, String text) {

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

        checkUserService.check(requestBody, chatId);
    }
}
