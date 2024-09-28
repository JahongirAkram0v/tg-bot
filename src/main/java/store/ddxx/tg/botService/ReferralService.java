package store.ddxx.tg.botService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.ddxx.tg.model.User;
import store.ddxx.tg.service.UserService;

@Component
@RequiredArgsConstructor
public class ReferralService {

    private final UserService userService;
    private final SendService send;

    private Long referralId;

    public boolean isReferral(String referral) {
        if (referral.length() > 8) referralId = Long.parseLong(referral.substring(7));
        return referral.startsWith("/start") && userService.existsByChatId(referralId);
    }

    public void referral(Long chatId, User user) {

        user.setChatId(chatId);
        user.setToken(user.getToken() + 7);
        userService.save(user);

        User referralUser = userService.findById(referralId);
        referralUser.setToken(referralUser.getToken() + 11);
        userService.save(referralUser);

        send.botSendTextMessage(chatId, "Referral link orqali kirib 7 ta tokenni qolga kiritdingiz.");
        send.botSendTextMessage(referralUser.getChatId(), "11 ta tokenni qolga kiritdingiz");
    }
}
