package store.ddxx.tg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ddxx.tg.model.User;
import store.ddxx.tg.model.UserState;


public interface UserRepo extends JpaRepository<User, Long> {

}
