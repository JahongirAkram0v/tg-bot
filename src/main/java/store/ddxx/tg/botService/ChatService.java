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
        if (message.getText() == null || message.getText().startsWith("/start") || botCommands.isBotCommand(message.getText())) {
            send.deleteMessage(message);
            return;
        }
        Long activeChatUsersId = activeChatUsersService.findConnectedUserId(user.getChatId());
        send.activeUsersSendTextMessage(activeChatUsersId, message.getText(), user.getName());
    }
}