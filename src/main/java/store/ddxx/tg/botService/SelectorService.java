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

    public void selector(User user) {

        User waitingUser = userService.findFirstByUserState();
        waitingUser.setUserState(UserState.CHAT);
        user.setUserState(UserState.CHAT);

        ActiveChatUsers activeChatUsers = ActiveChatUsers.builder()
                .userId1(user.getChatId())
                .userId2(waitingUser.getChatId())
                .build();
        activeChatUsersService.save(activeChatUsers);

        send.botSendTextMessage(user.getChatId(), "Suhbatni boshlashingiz mumkin.");
        send.botSendTextMessage(waitingUser.getChatId(), "Suhbatni boshlashingiz mumkin.");
    }
}
