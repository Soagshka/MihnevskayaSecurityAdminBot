package ru.home.security_admin_bot.dao;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "security_admin_bot", catalog = "securuty_bot_mh")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    @NonNull
    private int userId;
    @NonNull
    private Long chatId;
}
