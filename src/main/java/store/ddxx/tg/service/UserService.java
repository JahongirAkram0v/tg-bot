package store.ddxx.tg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ddxx.tg.model.User;
import store.ddxx.tg.repository.UserRepo;

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
        return user.getChatId() != null && user.getName() != null;
    }

    public Long findFirstChatIdByUserState() {return repo.findFirstChatIdByUserState();}

    public boolean existsByChatId(Long chatId) {return repo.existsByChatId(chatId);}

    public Long findCountUsers() {return repo.findCountUsers();}

    public Long findTokenByUserId(Long chatId) {return repo.findTokenByUserId(chatId);}

    public List<Long> findAllChatId() {return repo.findAllChatId();}

    public List<Long> findChatIdByUserState() {return repo.findChatIdByUserState();}
}
