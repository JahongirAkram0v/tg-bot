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
import store.ddxx.tg.service.UserService;

import static store.ddxx.tg.model.UserState.*;

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
    private final AdminService adminService;
    private final KeyboardService keyboardService;
    private final DeleteService deleteMessage;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        if (!update.hasMessage()) {
            return null;
        }

        Message message = update.getMessage();

        Long chatId = message.getChatId();
        String text = message.getText();

        if (text == null) {
            deleteMessage.deleteMessage(message);
            return null;
        }

        User user = userService.findById(chatId);

        if (user.getChatId() == null && referralService.isReferral(text)) {
            referralService.referral(chatId, user);
        }

        if (!userService.isActivated(user)) {
            singUp.singUp(user, message);
            return null;
        }

        if ( botCommands.isBotCommand(text) && (user.getUserState().equals(ACTIVATE) || user.getUserState().equals(ADMIN))) {
            botCommands.botCommand(user, text);
        }

        if (keyboardService.isKeyboard(text)) {
            keyboardService.keyboard(user, text);
        }

        if (user.getUserState().equals(ACTIVATE)) {
            bridgeService.bridge(user);
            return null;
        }

        if (user.getUserState().equals(START_CHAT)) {
            startChatService.startChat(user);
        }

        if (user.getUserState().equals(WAITING)) {
            waitingService.waiting(user);
            return null;
        }

        if (user.getUserState().equals(SELECTOR)) {
            selectorService.selector(user);
            return null;
        }

        if (user.getUserState().equals(CHAT)) {
            chatService.chatText(user, text);
            return null;
        }

        if (user.getUserState().equals(ADMIN) && !botCommands.isBotCommand(text)) {
            adminService.sendText(text);
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
