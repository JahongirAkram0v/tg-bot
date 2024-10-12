package store.ddxx.tg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.ddxx.tg.model.User;

import java.util.List;


public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByChatId(Long chatId);

    @Query("SELECT u.chatId FROM _user u WHERE u.userState = 'WAITING'")
    Long findFirstChatIdByUserState();

    @Query("SELECT COUNT(*) FROM _user")
    Long findCountUsers();

    @Query("SELECT t.token " +
            "FROM _user t " +
            "WHERE t.chatId = :chatId")
    Long findTokenByUserId(@Param("chatId") Long chatId);

    @Query("SELECT chatId FROM _user")
    List<Long> findAllChatId();

    @Query("SELECT u.chatId FROM _user u WHERE u.userState = 'NEXT'")
    List<Long> findChatIdByUserState();
}
