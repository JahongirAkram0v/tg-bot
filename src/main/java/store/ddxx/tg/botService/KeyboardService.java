package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.ActiveChatUsersService;
import store.ddxx.tg.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static store.ddxx.tg.model.UserState.*;

@Component
@RequiredArgsConstructor
public class KeyboardService {

    private final List<String> keyboards = Arrays.asList("⚡️ chat", "➡️ next", "\uD83D\uDED1 stop");

    private final SendService send;
    private final UserService userService;
    private final ActiveChatUsersService activeChatUsersService;
    private final StartChatService startChatService;
    private final DeleteService deleteService;


    public boolean isKeyboard(String message) {
        return keyboards.stream().anyMatch(keyboard -> keyboard.equals(message));
    }

    public void keyboard(User user, Message message) {

        switch (message.getText()) {
            case "⚡️ chat" -> {
                if (user.getUserState().equals(ACTIVATE)) {
                    user.setUserState(START_CHAT);
                }
            }
            case "➡️ next" -> {
                if (user.getUserState().equals(CHAT)) {
                    if (user.getClickedTime().isAfter(LocalDateTime.now().minusSeconds(15))) {
                        deleteService.deleteMessage(message);
                        return;
                    }
                    user.setClickedTime(LocalDateTime.now());
                    userService.save(user);
                    setController(user, START_CHAT, "Suhbatdosh almashtirildi, Suhbatni bo'shlash uchun istalgan tugmani bosing.");
                    send.botSendTextMessage(user.getChatId(), "Suhbat boshlansa xabar beriladi");
                }
            }
            case "\uD83D\uDED1 stop" -> {
                if (user.getUserState().equals(CHAT)) {
                    setController(user, ACTIVATE, "Suhbat yakunlandi, Suhbatni bo'shlash uchun istalgan tugmani bosing.");
                } else if (user.getUserState().equals(WAITING)) {
                    startChatService.setChanger(true);
                    user.setUserState(ACTIVATE);
                }
            }
        }
        userService.save(user);
    }

    private void setController(User user, UserState userState, String text) {
        Long activeChatUsersId = activeChatUsersService.findConnectedUserId(user.getChatId());

        send.controlReplyKeyboardMarkup(activeChatUsersId, text);
        User waitingUser = userService.findById(activeChatUsersId);

        if (!waitingUser.getUserState().equals(ACTIVATE)) {
            waitingUser.setUserState(NEXT);
        }

        user.setUserState(userState);
        activeChatUsersService.deleteByUserId1OrUserId2(user.getChatId());
    }
}
