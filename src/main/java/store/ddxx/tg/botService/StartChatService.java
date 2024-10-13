package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.UserService;

@Component
@RequiredArgsConstructor
public class StartChatService {

    @Setter
    private boolean changer = true;
    private final UserService userService;

    public void startChat(User user) {

        changer = userService.findFirstChatIdByUserState().isEmpty();

        if (changer && userService.findChatIdByUserState().size() >= 2) {
            Long waitingUserId = userService.findChatIdByUserState().getFirst();
            User waitingUser = userService.findById(waitingUserId);
            waitingUser.setUserState(UserState.WAITING);
            userService.save(waitingUser);
            changer = false;
        }

        if (changer) {
            user.setUserState(UserState.WAITING);
        } else {
            user.setUserState(UserState.SELECTOR);
        }
        userService.save(user);
    }
}
