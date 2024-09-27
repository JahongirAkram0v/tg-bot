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

    public boolean isReferral(String referral) {
        return referral.length() > 7
                && referral.startsWith("/start")
                && userService.findById(Long.parseLong(referral.substring(7))).getChatId() != null;
    }

    public void referral(String referral, Long chatId, User user) {
        Long referralId = Long.parseLong(referral.substring(7));

        User referralUser = userService.findById(referralId);
        referralUser.setToken(referralUser.getToken() + 11);
        userService.save(referralUser);

        user.setChatId(chatId);
        user.setToken(user.getToken() + 7);
        userService.save(user);
        send.botSendTextMessage(chatId, "Referral link orqali kirib 7 ta tokenni qolga kiritdingiz.");
        send.botSendTextMessage(referralUser.getChatId(), "11 ta tokenni qolga kiritdingiz");
    }
}
