package store.ddxx.tg.botService;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.ActiveChatUsersService;
import store.ddxx.tg.service.UserService;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BotCommandsService {

    private final List<String> botCommands = Arrays.asList(
            "/info", "/referral", "⚡️ chat", "➡️ next", "\uD83D\uDED1 stop", "/admin", "/exit");
    private final Dotenv dotenv = Dotenv.load();
    private final String adminId = dotenv.get("ADMIN_ID");

    private final SendService send;
    private final UserService userService;
    private final ActiveChatUsersService activeChatUsersService;
    private final StartChatService startChatService;


    public boolean isBotCommand(String message) {
        return botCommands.stream().anyMatch(command -> command.equals(message));
    }

    public void botCommand(User user, String message) {

        switch (message) {
            case "/info" -> send.botSendTextMessage(
                    user.getChatId(),
                    """
                        
                        👥 Umumiy foydalanuvchilar: %d
                        🗣 Suhbatlashayotganlar: %d
                        💳 tokenlaringiz: %d
                        """.formatted(
                                userService.findCountUsers()
                            , 2 * activeChatUsersService.findCountActiveChatUsers()
                            , userService.findTokenByUserId(user.getChatId()))
            );
            case "/referral" -> send.botSendTextMessage(
                    user.getChatId(),
                    "https://t.me/chat_wx_bot?start=" + user.getChatId()
            );
            case "⚡️ chat" -> {
                if (user.getUserState().equals(UserState.ACTIVATE) || user.getUserState().equals(UserState.START_CHAT)) {
                    user.setUserState(UserState.START_CHAT);
                }
            }
            case "➡️ next" -> {
                if (user.getUserState().equals(UserState.CHAT)) {
                    Long activeChatUsersId = activeChatUsersService.findConnectedUserId(user.getChatId());
                    User waitingUser = userService.findById(activeChatUsersId);
                    send.controlReplyKeyboardMarkup(activeChatUsersId, "Suhbat yakunlandi, ➡️ next ni bosing");
                    user.setUserState(UserState.START_CHAT);
                    waitingUser.setUserState(UserState.START_CHAT);
                    activeChatUsersService.deleteByUserId1OrUserId2(user.getChatId());
                }
            }
            case "\uD83D\uDED1 stop" -> {
                if (user.getUserState().equals(UserState.CHAT)) {

                    Long activeChatUsersId = activeChatUsersService.findConnectedUserId(user.getChatId());
                    User waitingUser = userService.findById(activeChatUsersId);
                    send.controlReplyKeyboardMarkup(activeChatUsersId, "Suhbat yakunlandi. ➡️ next ni bosing");
                    user.setUserState(UserState.ACTIVATE);
                    waitingUser.setUserState(UserState.START_CHAT);
                    activeChatUsersService.deleteByUserId1OrUserId2(user.getChatId());

                } else if (user.getUserState().equals(UserState.WAITING)) {
                    startChatService.setChanger(!startChatService.isChanger());
                    user.setUserState(UserState.ACTIVATE);
                }
            }
            case "/admin" -> {
                if (adminId.equals(user.getChatId().toString())) {
                    user.setUserState(UserState.ADMIN);
                }
            }
            case "/exit" -> user.setUserState(UserState.ACTIVATE);
        }
        userService.save(user);

    }


}
