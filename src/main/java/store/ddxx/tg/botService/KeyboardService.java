package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.ActiveChatUsersService;
import store.ddxx.tg.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KeyboardService {

    private final List<String> keyboards = Arrays.asList("⚡️ chat", "➡️ next", "\uD83D\uDED1 stop");

    private final SendService send;
    private final UserService userService;
    private final ActiveChatUsersService activeChatUsersService;
    private final StartChatService startChatService;


    public boolean isKeyboard(String message) {
        return keyboards.stream().anyMatch(keyboard -> keyboard.equals(message));
    }

    public void keyboard(User user, String message) {

        switch (message) {
            case "⚡️ chat" -> {
                if (user.getUserState().equals(UserState.ACTIVATE)) {
                    user.setUserState(UserState.START_CHAT);
                }
            }
            case "➡️ next" -> {
                if ( user.getUserState().equals(UserState.CHAT)
                        && user.getClickedTime().isBefore(LocalDateTime.now().minusSeconds(15)) ) {
                    setController(user, UserState.START_CHAT, "➡️ next ni bosing");
                    user.setClickedTime(LocalDateTime.now());
                    send.botSendTextMessage(user.getChatId(), "Suhbat boshlansa xabar beriladi");
                }
            }
            case "\uD83D\uDED1 stop" -> {
                if (user.getUserState().equals(UserState.CHAT)) {
                    setController(user, UserState.ACTIVATE, "Suhbat yakunlandi. ➡️ next ni bosing");
                } else if (user.getUserState().equals(UserState.WAITING)) {
                    startChatService.setChanger(!startChatService.isChanger());
                    user.setUserState(UserState.ACTIVATE);
                }
            }
        }
        userService.save(user);
    }

    private void setController(User user, UserState userState, String text) {
        Long activeChatUsersId = activeChatUsersService.findConnectedUserId(user.getChatId());
        User waitingUser = userService.findById(activeChatUsersId);
        send.controlReplyKeyboardMarkup(activeChatUsersId, text);
        user.setUserState(userState);
        waitingUser.setUserState(UserState.START_CHAT);
        activeChatUsersService.deleteByUserId1OrUserId2(user.getChatId());
    }

}
