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

        if (changer && userService.findChatIdByUserState().size() >= 2) {
            Long waitingUserId = userService.findChatIdByUserState().getFirst();
            User waitingUser = userService.findById(waitingUserId);
            waitingUser.setUserState(UserState.WAITING);
            userService.save(waitingUser);
            changer = false;
        }

        if (changer) {
            user.setUserState(UserState.WAITING);
            changer = false;
        } else {
            user.setUserState(UserState.SELECTOR);
            changer = true;
        }
        userService.save(user);
    }
}
