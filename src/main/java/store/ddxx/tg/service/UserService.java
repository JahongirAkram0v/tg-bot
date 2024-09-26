package store.ddxx.tg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;
import store.ddxx.tg.repository.UserRepo;

import java.time.LocalDateTime;
import java.util.List;

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

    public User findFirstByUserState() {return repo.findFirstByUserState(UserState.WAITING);}

    public Long findCountUsers() {return repo.findCountUsers();}

    public Long findTokenByUserId(Long chatId) {return repo.findTokenByUserId(chatId);}

    public List<Long> findAllChatId() {return repo.findAllChatId();}

    public LocalDateTime findClickedTime(Long chatId) {return repo.findClickedTime(chatId);}
}
