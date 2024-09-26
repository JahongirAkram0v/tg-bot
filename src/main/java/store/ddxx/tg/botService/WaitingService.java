package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import store.ddxx.tg.model.User;

@Component
@RequiredArgsConstructor
public class WaitingService {

    private final SendService send;

    public void waiting(User user, Message message) {
        if (message.getText() == null || !message.getText().equals("⚡️ chat")) {
            send.deleteMessage(message);
            return;
        }
        send.controlReplyKeyboardMarkup(user.getChatId(), "Kutib turing. Suhbat boshlansa xabar beriladi");
    }
}
