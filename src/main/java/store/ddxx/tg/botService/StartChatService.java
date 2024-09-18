package store.ddxx.tg.botService;

import lombok.Getter;
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
    @Getter
    private boolean changer = true;
    private final UserService userService;

    public void startChat(User user) {

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
