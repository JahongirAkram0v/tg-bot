package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;

@Component
@RequiredArgsConstructor
public class BridgeService {

    private final SendService send;

    public void bridge(User user) {
        send.chatReplyKeyboardMarkup(user.getChatId(),
                "Yana bir bor salom " + user.getName()
                + ".\nSuhbatni boshlamoqchi bo'lsangiz ⚡️ chat ni bosing.");
    }
}
