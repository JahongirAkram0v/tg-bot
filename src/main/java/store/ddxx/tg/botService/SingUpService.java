package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.UserService;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SingUpService {

    private final UserService service;
    private final SendService send;

    public void singUp(User user, Message message) {
        switch (user.getUserState()) {
            case UserState.START -> signUpForChatId(user, message.getChatId());
            case UserState.NAME -> signUpForName(user, message.getText());
        }
        service.save(user);
    }

    private void signUpForChatId(User user, Long chatId) {
        user.setChatId(chatId);
        user.setUserState(UserState.NAME);
        String text = """
                Assalomu alaykum botga xush kelibsiz.

                Iltimos asl ismingizni kiriting.""";

        send.botSendTextMessage(user.getChatId(), text);
    }

    private void signUpForName(User user, String name) {
        if (name.equals("/start")) return;
        user.setName(fixName(name));
        signUpDone(user);
    }

    private String fixName(String name) {
        return name
                .chars()
                .filter(Character::isLetter)
                .mapToObj(c -> String.valueOf((char) c))
                .limit(20)
                .collect(Collectors.joining());
    }

    private void signUpDone(User user) {
        user.setUserState(UserState.ACTIVATE);
        send.chatReplyKeyboardMarkup(user.getChatId(), "Subhatni boshlash uchun ⚡️ chat ni bosing");
    }
}
