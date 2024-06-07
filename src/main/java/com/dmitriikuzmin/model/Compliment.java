package com.dmitriikuzmin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compliment")
public class Compliment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(unique = true)
    private String text;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "telegram_users_compliments",
            joinColumns = @JoinColumn(name = "compliment_id"),
            inverseJoinColumns = @JoinColumn(name = "telegram_user_id")
    )
    private List<TelegramUser> telegramUsers;

    public void addTelegramUser(TelegramUser telegramUser) {
        this.telegramUsers.add(telegramUser);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Compliment that = (Compliment) object;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
