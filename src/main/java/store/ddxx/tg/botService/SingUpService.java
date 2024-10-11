package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.model.VideoFileId;
import store.ddxx.tg.service.UserService;
import store.ddxx.tg.service.VideoFileIdService;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SingUpService {

    private final UserService service;
    private final BotCommandsService botCommands;
    private final ReferralService referralService;
    private final DeleteService deleteService;
    private final SendService send;
    private final VideoFileIdService videoFileIdService;

    public void singUp(User user, Message message) {

        switch (user.getUserState()) {
            case UserState.START -> signUpForChatId(user, message.getChatId());
            case UserState.NAME -> signUpForName(user, message);
        }
        service.save(user);
    }

    private void signUpForChatId(User user, Long chatId) {
        user.setChatId(chatId);
        user.setUserState(UserState.NAME);
        String text = """
                Assalomu alaykum botga xush kelibsiz.

                Iltimos ismingizni kiriting. [A-Z, a-z]""";

        if (videoFileIdService.find() != null) {
            VideoFileId videoFileId = videoFileIdService.find();
            send.sendSpecialVideo(user.getChatId().toString(), videoFileId.getFileId());
        }
        send.botSendTextMessage(user.getChatId(), text);
    }

    private void signUpForName(User user, Message message) {
        String text = message.getText();
        if (botCommands.isBotCommand(text)) {
            deleteService.deleteMessage(message);
            return;
        }

        if (text.equals("/start") || referralService.isReferral(text)) {
            signUpForChatId(user, user.getChatId());
            return;
        }

        user.setName(fixName(message.getText()).isEmpty() ? "User" : fixName(message.getText()));
        signUpDone(user);
    }

    public String fixName(String name) {
        return name
                .chars()
                .filter(Character::isLetter)
                .mapToObj(c -> String.valueOf((char) c))
                .limit(20)
                .collect(Collectors.joining());
    }

    private void signUpDone(User user) {
        user.setUserState(UserState.ACTIVATE);
        send.chatReplyKeyboardMarkup(user.getChatId(), "\uD83E\uDD16:\t\n" +
                "Subhatni boshlash uchun ⚡️ chat tugmasini bosing");
    }
}
