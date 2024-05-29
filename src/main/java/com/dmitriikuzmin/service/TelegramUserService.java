package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.TelegramUser;

public interface TelegramUserService {

    TelegramUser add(TelegramUser telegramUser);

    TelegramUser getByChatId(Long chatId);

    TelegramUser update(TelegramUser telegramUser);
}
