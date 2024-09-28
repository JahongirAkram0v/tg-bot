package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.ActiveChatUsersService;
import store.ddxx.tg.service.UserService;

@Component
@RequiredArgsConstructor
public class ChatService {

    private final SendService send;
    private final ActiveChatUsersService activeChatUsersService;
    private final UserService userService;

    public void chatText(User user, String text) {
        Long activeChatUsersId = activeChatUsersService.findConnectedUserId(user.getChatId());
        if (text.length() > 1024) {
            text = String.format("%s...", text.substring(0, 1021));
        }
        send.activeUsersSendTextMessage(activeChatUsersId, text, user.getName());

        if (userService.findById(activeChatUsersId).getUserState().equals(UserState.ACTIVATE)) {
            user.setUserState(UserState.START_CHAT);
            userService.save(user);
            send.controlReplyKeyboardMarkup(
                    user.getChatId(),
                    "Suhbatdosh qayta topilyapti, qayta boshlash uchun istalgan tugmani bosing.");
        }

    }
}