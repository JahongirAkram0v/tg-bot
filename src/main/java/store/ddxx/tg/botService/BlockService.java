package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.UserService;

@Component
@RequiredArgsConstructor
public class BlockService {

    private final UserService userService;
    private final SendService sendService;

    public void block(User user) {
        user.setUserState(UserState.ACTIVATE);
        userService.save(user);
        sendService.botSendTextMessage(user.getChatId(), "Qayta kirganingizdan xursandman!");
    }
}
