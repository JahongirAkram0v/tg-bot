package store.ddxx.tg.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.ddxx.tg.model.ActiveChatUsers;

public interface ActiveChatUsersRepo extends JpaRepository<ActiveChatUsers, Long> {

    @Query("SELECT CASE WHEN a.userId1 = :userId THEN a.userId2 ELSE a.userId1 END " +
            "FROM ActiveChatUsers a WHERE :userId IN (a.userId1, a.userId2)")
    Long findConnectedUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ActiveChatUsers WHERE userId1 = :userId OR userId2 = :userId")
    void deleteByUserId1OrUserId2(@Param("userId") Long userId);

    @Query("SELECT COUNT(*) FROM ActiveChatUsers")
    Long findCountActiveChatUsers();

}