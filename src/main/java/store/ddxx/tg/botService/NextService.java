package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.UserService;

@Component
@RequiredArgsConstructor
public class NextService {

    private final UserService userService;

    public void next(User user) {
        user.setUserState(UserState.START_CHAT);
        userService.save(user);
    }
}
