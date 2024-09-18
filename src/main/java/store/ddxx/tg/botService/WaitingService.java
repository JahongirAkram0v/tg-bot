package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;

@Component
@RequiredArgsConstructor
public class WaitingService {

    private final SendService send;

    public void waiting(User user) {
        send.controlReplyKeyboardMarkup(user.getChatId(), "Kutib turing. Suhbat boshlansa xabar beriladi");
    }
}
