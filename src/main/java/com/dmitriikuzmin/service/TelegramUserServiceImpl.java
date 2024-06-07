package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.TelegramUser;
import com.dmitriikuzmin.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {
    private TelegramUserRepository telegramUserRepository;

    @Autowired
    public void setTelegramUserRepository(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    public TelegramUser add(TelegramUser telegramUser) {
        try {
            return this.telegramUserRepository.save(telegramUser);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Login already exist");
        }
    }

    @Override
    public TelegramUser getByChatId(Long chatId) {
        return this.telegramUserRepository.findByChatId(chatId)
                .orElseThrow(() -> new IllegalArgumentException("wrong chatId"));
    }

    @Override
    public TelegramUser update(TelegramUser telegramUser) {
        TelegramUser base = this.getByChatId(telegramUser.getChatId());
        base.setLogin(telegramUser.getLogin());
        base.setName(telegramUser.getName());
        base.setAge(telegramUser.getAge());
        base.setStep(telegramUser.getStep());
        base.setCompliments(telegramUser.getCompliments());
        try {
            return this.telegramUserRepository.save(base);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Login already exist");
        }
    }
}
