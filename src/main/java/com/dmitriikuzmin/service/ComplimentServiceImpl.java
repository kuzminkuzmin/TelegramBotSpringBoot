package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Compliment;
import com.dmitriikuzmin.model.DataList;
import com.dmitriikuzmin.model.TelegramUser;
import com.dmitriikuzmin.repository.ComplimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplimentServiceImpl implements ComplimentService {
    private ComplimentRepository complimentRepository;
    private DataList dataList;
    private TelegramUserService telegramUserService;

    @Autowired
    public void setComplimentRepository(ComplimentRepository complimentRepository) {
        this.complimentRepository = complimentRepository;
    }

    @Autowired
    public void setDataList(DataList dataList) {
        this.dataList = dataList;
        for (Compliment compliment : this.dataList.getCompliments()) {
            try {
                this.add(compliment);
            } catch (Exception ignored) {
            }
        }
    }

    @Autowired
    public void setTelegramUserService(TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
    }

    @Override
    public Compliment add(Compliment compliment) {
        try {
            return this.complimentRepository.save(compliment);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Data integrity violation");
        }
    }

    @Override
    public Compliment get(long id) {
        return this.complimentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compliment not found"));
    }

    @Override
    public List<Compliment> getAllCompliments() {
        return this.complimentRepository.findAll();
    }

    @Override
    public Compliment getRandomCompliment(long chatId) {
        try {
            TelegramUser telegramUser = this.telegramUserService.getByChatId(chatId);
            List<Compliment> unused = this.getAllCompliments();
            List<Compliment> used = this.complimentRepository.findByTelegramUsers(telegramUser);
            unused.removeAll(used);
            if (unused.isEmpty()) {
                resetCompliments(chatId);
                unused = this.getAllCompliments();
            }
            Compliment compliment = unused.get((int) (Math.random() * unused.size()));
            compliment.addTelegramUser(telegramUser);
            this.update(compliment);
            return compliment;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("No user found");
        }
    }

    @Override
    public Compliment update(Compliment compliment) {
        Compliment base = this.get(compliment.getId());
        base.setText(compliment.getText());
        base.setTelegramUsers(compliment.getTelegramUsers());
        try {
            this.complimentRepository.save(base);
            return base;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Data integrity violation");
        }
    }

    private void resetCompliments(long chatId) {
        try {
            TelegramUser telegramUser = this.telegramUserService.getByChatId(chatId);
            telegramUser.clearCompliments();
            telegramUserService.update(telegramUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No user found");
        }
    }
}
