package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import store.ddxx.tg.model.User;
import store.ddxx.tg.service.UserService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class WaitingService {

    private final SendService send;
    private final UserService userService;

    public void waiting(User user, Message message) {
        boolean b = user.getClickedTime().isAfter(LocalDateTime.now().minusSeconds(10));
        if (message.getText() != null && b && !message.getText().equals("⚡️ chat")) {
            send.deleteMessage(message);
            return;
        }
        user.setClickedTime(LocalDateTime.now());
        userService.save(user);
        send.controlReplyKeyboardMarkup(user.getChatId(), "Kutib turing. Suhbat boshlansa xabar beriladi");
    }
}
