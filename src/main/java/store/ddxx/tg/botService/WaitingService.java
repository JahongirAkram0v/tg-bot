package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import store.ddxx.tg.model.User;
import store.ddxx.tg.service.UserService;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class WaitingService {

    private final SendService send;
    private final DeleteService deleteService;
    private final UserService userService;

    public void waiting(User user, Message message) {
        if (!(message.getText().equals("⚡️ chat") || message.getText().equals("/start"))
                && user.getClickedTime().isAfter(LocalTime.now().minusSeconds(15))) {
            deleteService.deleteMessage(message);
            return;
        }
        user.setClickedTime(LocalTime.now());
        userService.save(user);
        send.controlReplyKeyboardMarkup(user.getChatId(), "\uD83E\uDD16:\t\n" +
                "Kutib turing. Suhbat boshlansa xabar beriladi");
    }
}
