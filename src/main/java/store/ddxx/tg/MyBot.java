package store.ddxx.tg;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import store.ddxx.tg.botService.SingUpService;
import store.ddxx.tg.model.User;
import store.ddxx.tg.service.UserService;

@Component
@RequiredArgsConstructor
public class MyBot extends TelegramWebhookBot {

    Dotenv dotenv = Dotenv.load();
    private final String botUsername = dotenv.get("TELEGRAM_BOT_USERNAME");
    private final String botWebhookPath = dotenv.get("TELEGRAM_BOT_WEBHOOK_PATH");

    private final UserService userService;
    private final SingUpService singUp;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        if (!update.hasMessage()) {
            return null;
        }

        Message message = update.getMessage();

        Long chatId = message.getChatId();
        String text = message.getText();
        User user = userService.findById(chatId);

        if (!userService.isActivated(user)) {
            singUp.singUp(user, message);
            return null;
        }

        return null;
    }

    @Override
    public String getBotPath() {
        return botWebhookPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
