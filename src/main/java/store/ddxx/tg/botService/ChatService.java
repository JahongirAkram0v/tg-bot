package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;
import store.ddxx.tg.service.ActiveChatUsersService;

@Component
@RequiredArgsConstructor
public class ChatService {

    private final SendService send;
    private final ActiveChatUsersService activeChatUsersService;

    public void chatText(User user, String text) {
        Long activeChatUsersId = activeChatUsersService.findConnectedUserId(user.getChatId());
        send.activeUsersSendTextMessage(activeChatUsersId, text, user.getName());
    }
}