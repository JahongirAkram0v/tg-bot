package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;

@Component
@RequiredArgsConstructor
public class BridgeService {

    private final SendService send;

    public void bridge(User user) {
        send.chatReplyKeyboardMarkup(
                user.getChatId(),
                "\uD83E\uDD16:\t\n" +
                        "Yana bir bor salom " + user.getName()
                        + ".\nSuhbatni boshlamoqchi bo'lsangiz ⚡️ chat ni bosing."
        );
    }
}
