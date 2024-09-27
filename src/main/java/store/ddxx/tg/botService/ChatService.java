package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import store.ddxx.tg.model.User;
import store.ddxx.tg.service.ActiveChatUsersService;

@Component
@RequiredArgsConstructor
public class ChatService {

    private final SendService send;
    private final ActiveChatUsersService activeChatUsersService;
    private final BotCommandsService botCommands;

    public void chatText(User user, Message message) {
        String text = message.getText();
        if (text == null || text.startsWith("/start") || botCommands.isBotCommand(text)) {
            send.deleteMessage(message);
            return;
        }
        Long activeChatUsersId = activeChatUsersService.findConnectedUserId(user.getChatId());
        if (text.length() > 1024) {
            text = String.format("%s...", text.substring(0, 1021));
        }
        send.activeUsersSendTextMessage(activeChatUsersId, text, user.getName());
    }
}