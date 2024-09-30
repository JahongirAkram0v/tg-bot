package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.UserService;

@Component
@RequiredArgsConstructor
public class EditNameService {

    private final SendService sendService;
    private final UserService userService;
    private final SingUpService singUpService;

    public void editName(User user, String text) {
        int editNameCoast = 33;
        if (user.getToken() < editNameCoast) {
            sendService.botSendTextMessage(user.getChatId(),
                    "Tokenlaringiz yetmadi , sizda kamida " + editNameCoast + "ta token bo'lishi kerak.");
        } else {
            user.setToken(user.getToken() - editNameCoast);
            user.setName(singUpService.fixName(text));
            sendService.botSendTextMessage(user.getChatId(), user.getName() + " , Ismingizni muvaffaqiyatli o'zgartirdingiz!" );
        }
        user.setUserState(UserState.ACTIVATE);
        userService.save(user);
    }
}
