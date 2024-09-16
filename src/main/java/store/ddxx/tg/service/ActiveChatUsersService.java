package store.ddxx.tg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ddxx.tg.model.ActiveChatUsers;
import store.ddxx.tg.repository.ActiveChatUsersRepo;

@Service
@RequiredArgsConstructor
public class ActiveChatUsersService {

    private final ActiveChatUsersRepo repo;

    public Long findConnectedUserId(Long chatId) {
        return repo.findConnectedUserId(chatId);
    }

    public void save(ActiveChatUsers activeChatUsers) {
        repo.save(activeChatUsers);
    }

    public void deleteByUserId1OrUserId2(Long userId) {repo.deleteByUserId1OrUserId2(userId);}

    public Long findCountActiveChatUsers() {return repo.findCountActiveChatUsers();}
}
