package com.dmitriikuzmin.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "user")
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(unique = true)
    private long chatId;

    @NonNull
    private String telegramUserName;

    @NonNull
    private String step;

    @Column(unique = true)
    private String login;

    private String name;

    private int age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<History> history;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "telegram_users_compliments",
            joinColumns = @JoinColumn(name = "telegram_user_id"),
            inverseJoinColumns = @JoinColumn(name = "compliment_id")
    )
    private List<Compliment> compliments;

    public void clearCompliments() {
        this.compliments.clear();
    }
}
