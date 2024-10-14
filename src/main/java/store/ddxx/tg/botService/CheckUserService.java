package store.ddxx.tg.botService;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.service.UserService;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CheckUserService {

    private final Dotenv dotenv = Dotenv.load();
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserService userService;
    private final String url = dotenv.get("BASE_URL") + dotenv.get("TELEGRAM_BOT_TOKEN") + "/sendMessage";

    public void check(Map<String, Object> requestBody, Long chatId) {

        try {
            restTemplate.postForObject(url, requestBody, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {

                System.out.println("BLOCKLAGAN : " + chatId);
                User user = userService.findById(chatId);

                user.setUserState(UserState.BLOCK);
                userService.save(user);
                System.out.println("User ACTIVATE holatiga o'tkazildi.");

            }
            System.out.println(e.getMessage());
        }
    }
}
