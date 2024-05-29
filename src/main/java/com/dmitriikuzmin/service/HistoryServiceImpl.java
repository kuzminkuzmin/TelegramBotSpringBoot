package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.History;
import com.dmitriikuzmin.model.TelegramUser;
import com.dmitriikuzmin.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {
    private HistoryRepository historyRepository;
    private TelegramUserService telegramUserService;

    @Autowired
    public void setHistoryRepository(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Autowired
    public void setTelegramUserService(TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
    }

    @Override
    public History add(long chatId, History history) {
        TelegramUser user = this.telegramUserService.getByChatId(chatId);
        history.setUser(user);
        this.historyRepository.save(history);
        return history;
    }
}
