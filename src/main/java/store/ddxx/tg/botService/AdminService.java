package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.ActiveChatUsers;
import store.ddxx.tg.model.User;
import store.ddxx.tg.service.ActiveChatUsersService;
import store.ddxx.tg.service.UserService;

import static store.ddxx.tg.model.UserState.*;

@Component
@RequiredArgsConstructor
public class AdminService {

    private final SendService send;
    private final UserService userService;
    private final ActiveChatUsersService activeChatUsersService;

    public void sendText(String text){

        if (text.startsWith("chatId")) {
            send.botSendTextMessage(
                    Long.parseLong(text.substring(6, text.indexOf(" "))),
                    text.substring(text.indexOf(" ") + 1)
            );
        } else if (text.startsWith("connect")) {
            Long chatId1 = Long.parseLong(text.substring(7, text.indexOf("|")));
            Long chatId2 = Long.parseLong(text.substring(text.indexOf("|") + 1));
            User user1 = userService.findById(chatId1);
            User user2 = userService.findById(chatId2);
            if (user1.getUserState().equals(ACTIVATE)
                && user2.getUserState().equals(ACTIVATE)) {
                user1.setUserState(CHAT);
                user2.setUserState(CHAT);
                userService.save(user1);
                userService.save(user2);
                ActiveChatUsers  activeChatUsers = ActiveChatUsers.builder()
                        .userId1(chatId1)
                        .userId2(chatId2)
                        .build();
                activeChatUsersService.save(activeChatUsers);
                send.controlReplyKeyboardMarkup(chatId1, "Suhbatni boshlashingiz mumkin!");
                send.controlReplyKeyboardMarkup(chatId2, "Suhbatni boshlashingiz mumkin!");
            }
        } else if (text.startsWith("emoji")) {
            Long chatId = Long.parseLong(text.substring(5, text.indexOf(" ")));
            User user = userService.findById(chatId);
            user.setName(user.getName() + " " + text.substring(text.indexOf(" ") + 1));
            userService.save(user);
        } else if (text.equals("close")) {
            userService.findAllChatId()
                    .forEach(chatId -> send.sendTextMessage(chatId,
                            "⚠️ Bot vaqtingcha to'xtatilyapti ! ⚠️\n" +
                            "Agar qayta ishga tushirilsa xabar beriladi."));
        }
        else userService.findAllChatId()
                .forEach(chatId -> send.sendTextMessage(chatId, text));
    }

}
