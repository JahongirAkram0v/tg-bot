package store.ddxx.tg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;


public interface UserRepo extends JpaRepository<User, Long> {

    User findFirstByUserState(UserState state);

    @Query("SELECT COUNT(*) FROM _user")
    Long findCountUsers();

    @Query("SELECT t.token " +
            "FROM _user t " +
            "WHERE t.chatId = :chatId")
    Long findTokenByUserId(@Param("chatId") Long userId);
}
