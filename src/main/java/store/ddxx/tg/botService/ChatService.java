package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.ActiveChatUsersService;
import store.ddxx.tg.service.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatService {

    private final SendService send;
    private final ActiveChatUsersService activeChatUsersService;
    private final UserService userService;
    private final BotCommandsService botCommandsService;
    private final DeleteService deleteService;
    private final ReferralService referralService;

    public void chatText(User user, Message message) {

        if (message.getEntities() != null) {

            List<MessageEntity> entities = message.getEntities();

            for (MessageEntity entity : entities) {
                if ("url".equals(entity.getType()) || "mention".equals(entity.getType())){
                    deleteService.deleteMessage(message);
                    send.botSendTextMessage(user.getChatId(), "Siz bunday xabar yubora olmaysiz.");
                    return;
                }
            }
        }

        if (botCommandsService.isBotCommand(message.getText())) {
            deleteService.deleteMessage(message);
            return;
        }
        if (message.getText().equals("/start") || referralService.isReferral(message.getText())) {
            send.controlReplyKeyboardMarkup(user.getChatId(),
                    "Siz hali ham suhbat jarayonidasiz, - 'Qandaysiz!' deb yozing'");
            return;
        }
        String text = message.getText();
        Long activeChatUsersId = activeChatUsersService.findConnectedUserId(user.getChatId());
        if (text.length() > 1024) {
            text = String.format("%s...", text.substring(0, 1021));
        }
        send.activeUsersSendTextMessage(activeChatUsersId, text, user.getName());

        if (userService.findById(activeChatUsersId).getUserState().equals(UserState.BLOCK)) {
            user.setUserState(UserState.START_CHAT);
            userService.save(user);
            activeChatUsersService.deleteByUserId1OrUserId2(user.getChatId());
            send.controlReplyKeyboardMarkup(
                    user.getChatId(),
                    "Suhbatdosh qayta topilyapti, qayta boshlash uchun istalgan tugmani bosing.");
        }

    }
}