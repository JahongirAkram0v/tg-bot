package store.ddxx.tg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.repository.UserRepo;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;

    public User findById(Long chatId) {
        return repo.findById(chatId).orElse(new User());
    }

    public void save(User user) {
        repo.save(user);
    }

    public boolean isActivated(User user) {
        return user.getChatId() != null &&
                user.getName() != null &&
                user.getUserState() != null;
    }
}
