package store.ddxx.tg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {

    private final MyBot myBot;

    @Autowired
    public WebhookController(MyBot myBot) {
        this.myBot = myBot;
    }

    @PostMapping("/webhook")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return myBot.onWebhookUpdateReceived(update);
    }
}