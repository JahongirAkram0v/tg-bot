package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.service.UserService;

@Component
@RequiredArgsConstructor
public class AdminService {

    private final SendService send;
    private final UserService userService;

    public void sendTextAds(String text){
        userService.findAllChatId()
                .forEach(chatId -> send.adsTextMessage(chatId, text));
    }

}
