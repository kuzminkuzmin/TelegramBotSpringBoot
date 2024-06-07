package com.dmitriikuzmin.repository;

import com.dmitriikuzmin.model.Compliment;
import com.dmitriikuzmin.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplimentRepository extends JpaRepository<Compliment, Long> {
    List<Compliment> findByTelegramUsers(TelegramUser telegramUser);
}
