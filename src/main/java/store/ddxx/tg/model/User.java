package store.ddxx.tg.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "_user")
public class User {

    @Id
    private Long chatId;
    private Long token = 0L;
    @Column(length = 20)
    private String name;
    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private UserState userState = UserState.START;

}
