package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.TelegramUser;

import java.util.List;

public interface TelegramUserService {

    TelegramUser add(TelegramUser telegramUser);

    TelegramUser getByChatId(Long chatId);

    List<TelegramUser> getAll();

    TelegramUser update(TelegramUser telegramUser);
}
