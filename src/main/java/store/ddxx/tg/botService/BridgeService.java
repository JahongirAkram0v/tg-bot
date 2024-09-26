package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import store.ddxx.tg.model.User;

@Component
@RequiredArgsConstructor
public class BridgeService {

    private final SendService send;

    public void bridge(User user, Message message) {
        if (message.getText() == null) {
            send.deleteMessage(message);
            return;
        }
        send.chatReplyKeyboardMarkup(
                user.getChatId(),
                "Yana bir bor salom " + user.getName()
                        + ".\nSuhbatni boshlamoqchi bo'lsangiz ⚡️ chat ni bosing."
        );
    }
}
