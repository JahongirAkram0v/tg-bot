package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.ActiveChatUsers;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.ActiveChatUsersService;
import store.ddxx.tg.service.UserService;

@Component
@RequiredArgsConstructor
public class SelectorService {

    private final SendService send;
    private final UserService userService;
    private final ActiveChatUsersService activeChatUsersService;
    private final StartChatService startChatService;

    public void selector(User user) {

        Long waitingUserId = userService.findFirstChatIdByUserState().getLast();

        send.controlReplyKeyboardMarkup(waitingUserId,
                "Suhbatni boshlashingiz mumkin.\n - 'Salom' deb yozing.");

        User waitingUser = userService.findById(waitingUserId);

        if (waitingUser.getUserState().equals(UserState.BLOCK)) {
            startChatService.setChanger(true);
            user.setUserState(UserState.START_CHAT);
            userService.save(user);
            send.controlReplyKeyboardMarkup(
                    user.getChatId(),
                    "Suhbatdosh qayta topilyapti, qayta boshlash uchun istalgan tugmani bosing.");
            return;
        }

        user.setUserState(UserState.CHAT);
        waitingUser.setUserState(UserState.CHAT);

        send.controlReplyKeyboardMarkup(user.getChatId(),
                "Suhbatni boshlashingiz mumkin.\n - 'Salom' deb yozing.");

        ActiveChatUsers activeChatUsers = ActiveChatUsers.builder()
                .userId1(user.getChatId())
                .userId2(waitingUser.getChatId())
                .build();
        activeChatUsersService.save(activeChatUsers);
    }
}
