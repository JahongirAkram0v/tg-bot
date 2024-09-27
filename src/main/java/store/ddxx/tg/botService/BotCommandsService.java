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
            "/info", "/referral", "/admin", "/exit", "⚡️ chat", "➡️ next", "\uD83D\uDED1 stop");
    private final Dotenv dotenv = Dotenv.load();
    private final String adminId = dotenv.get("ADMIN_ID");
    private final String username = dotenv.get("TELEGRAM_BOT_USERNAME");

    private final SendService send;
    private final UserService userService;
    private final ActiveChatUsersService activeChatUsersService;


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
            case "/referral" -> {
                String text = "https://t.me/" + username + "?start=" + user.getChatId() +
                        "\n\nBu bot orqali siz tasodifiy insonlar bilan suhbatlashishingiz mumkin," +
                        "yuqoridagi linkni bosish orqali qo'shimcha tokenlarga ega bo'lasiz.";
                send.botSendTextMessage(user.getChatId(), text);
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
