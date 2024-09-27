package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.service.UserService;

@Component
@RequiredArgsConstructor
public class AdminService {

    private final SendService send;
    private final UserService userService;

    public void sendText(String text){

        if (text.startsWith("chatId")) {
            send.botSendTextMessage(
                    Long.parseLong(text.substring(6, text.indexOf(" "))),
                    text.substring(text.indexOf(" ") + 1)
            );
        }
        else userService.findAllChatId()
                .forEach(chatId -> send.sendTextMessage(chatId, text));
    }

}
