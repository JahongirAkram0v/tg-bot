package store.ddxx.tg;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import store.ddxx.tg.botService.*;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.UserService;

@Component
@RequiredArgsConstructor
public class MyBot extends TelegramWebhookBot {

    private final Dotenv dotenv = Dotenv.load();
    private final String botUsername = dotenv.get("TELEGRAM_BOT_USERNAME");
    private final String botWebhookPath = dotenv.get("TELEGRAM_BOT_WEBHOOK_PATH");

    private final UserService userService;
    private final SingUpService singUp;
    private final BotCommandsService botCommands;
    private final BridgeService bridgeService;
    private final StartChatService startChatService;
    private final WaitingService waitingService;
    private final SelectorService selectorService;
    private final ChatService chatService;
    private final ReferralService referralService;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        if (!update.hasMessage()) {
            return null;
        }

        Message message = update.getMessage();

        Long chatId = message.getChatId();
        String text = message.getText();
        User user = userService.findById(chatId);

        if (text != null && user.getChatId() == null && referralService.isReferral(text)) {
            referralService.referral(text, chatId, user);
        }

        if (text != null && userService.isActivated(user) && botCommands.isBotCommand(text)) {
            botCommands.botCommand(user, text);
        }

        if (text != null && !userService.isActivated(user) && !botCommands.isBotCommand(text)) {
            singUp.singUp(user, message);
            return null;
        }

        if (user.getUserState().equals(UserState.ACTIVATE)) {
            bridgeService.bridge(user);
            return null;
        }

        if (user.getUserState().equals(UserState.START_CHAT)) {
            startChatService.startChat(user);
        }

        if (user.getUserState().equals(UserState.WAITING)) {
            waitingService.waiting(user);
            return null;
        }

        if (user.getUserState().equals(UserState.SELECTOR)) {
            selectorService.selector(user);
            return null;
        }

        if (
                text != null && !text.equals("/start")
                && user.getUserState().equals(UserState.CHAT) && !botCommands.isBotCommand(text)
        ) {
            chatService.chatText(user, text);
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
