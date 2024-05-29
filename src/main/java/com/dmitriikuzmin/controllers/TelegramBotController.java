package com.dmitriikuzmin.controllers;

import com.dmitriikuzmin.model.History;
import com.dmitriikuzmin.model.TelegramUser;
import com.dmitriikuzmin.service.HistoryService;
import com.dmitriikuzmin.service.TelegramUserService;
import com.dmitriikuzmin.util.Action;
import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@BotController
public class TelegramBotController implements TelegramMvcController {
    @Value("${bot.token}")
    private String token;
    private Keyboard replyKeyboardMarkupReg;
    private Keyboard replyKeyboardMarkup;
    private TelegramUserService telegramUserService;
    private HistoryService historyService;

    @Autowired
    public void setTelegramUserService(TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
    }

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostConstruct
    public void init() {
        this.replyKeyboardMarkupReg = new ReplyKeyboardMarkup(
                "/register")
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);

        this.replyKeyboardMarkup = new ReplyKeyboardMarkup(
                "/next", "/all", "/photos")
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
    }

    @Override
    public String getToken() {
        return this.token;
    }

    private SendMessage sendMessageWithReg(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.replyMarkup(replyKeyboardMarkupReg);
        return sendMessage.parseMode(ParseMode.HTML);
    }

    private SendMessage sendMessageWithReply(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        return sendMessage.parseMode(ParseMode.HTML);
    }

    private SendMessage sendMessageWithKeyboard(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.replyMarkup(replyKeyboardMarkup);
        return sendMessage.parseMode(ParseMode.HTML);
    }

    @BotRequest(value = "/start", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest start(User user, Chat chat) {
        try {
            TelegramUser telegramUser = this.telegramUserService.getByChatId(chat.id());
            if (telegramUser.getStep().equals("start")) {
                return sendMessageWithReg(telegramUser.getChatId(), "Please register");
            } else if (telegramUser.getStep().equals("completed")) {
                return sendMessageWithKeyboard(telegramUser.getChatId(), "Choose option:");
            } else {
                return sendMessageWithReply(telegramUser.getChatId(), "Registration uncompleted. To complete registration send /register");
            }
        } catch (IllegalArgumentException e) {
            TelegramUser telegramUser = new TelegramUser(chat.id(), user.username(), "start");
            this.telegramUserService.add(telegramUser);
            this.historyService.add(telegramUser.getChatId(), new History(Action.START));
            return sendMessageWithReg(telegramUser.getChatId(), "Please register");
        }
    }

    /**
     * Бот предлагает пользователю клавиатуру с кнопкой /register которая позволяет ввести логин(login),
     * затем имя пользователя(name) и затем возраст(age), которые записываются в базу данных
     *
     * @param user
     * @param chat
     * @return
     */
    @BotRequest(value = "/register", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest register(User user, Chat chat) {
        TelegramUser telegramUser = this.telegramUserService.getByChatId(chat.id());
        this.historyService.add(telegramUser.getChatId(), new History(Action.REGISTER));
        if (telegramUser.getStep().equals("start")) {
            return sendMessageWithReply(telegramUser.getChatId(), "Enter login:");
        } else if (telegramUser.getStep().equals("login")) {
            return sendMessageWithReply(telegramUser.getChatId(), "Enter name:");
        } else if (telegramUser.getStep().equals("age")) {
            return sendMessageWithReply(telegramUser.getChatId(), "Enter age:");
        } else {
            return sendMessageWithReply(telegramUser.getChatId(), "Enter login:");
        }
    }

    /**
     * При выборе кнопки /next бот отправляет пользователю следующий случайный комплимент из списка,
     * при этом сохраняет в базу, что данный комплимент пользователь выбирал и хранит дату и время выбора
     *
     * @param user
     * @param chat
     * @return
     */
    @BotRequest(value = "/next", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest next(User user, Chat chat) {
        TelegramUser telegramUser = this.telegramUserService.getByChatId(chat.id());
        if (telegramUser.getStep().equals("completed")) {

            this.historyService.add(telegramUser.getChatId(), new History(Action.NEXT));
            return sendMessageWithKeyboard(telegramUser.getChatId(), "next");
        } else {
            return sendMessageWithReply(telegramUser.getChatId(), "Registration uncompleted. To complete registration send /register");
        }
    }

    /**
     * При выборе кнопки /all бот отправляет все комплименты
     *
     * @param user
     * @param chat
     * @return
     */
    @BotRequest(value = "/all", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest all(User user, Chat chat) {
        TelegramUser telegramUser = this.telegramUserService.getByChatId(chat.id());
        if (telegramUser.getStep().equals("completed")) {

            this.historyService.add(telegramUser.getChatId(), new History(Action.ALL));
            return sendMessageWithKeyboard(telegramUser.getChatId(), "all");
        } else {
            return sendMessageWithReply(telegramUser.getChatId(), "Registration uncompleted. To complete registration send /register");
        }
    }

    /**
     * Добавить кнопку /photos по нажатию на которую пользователю отображается список всех фотографий
     * из папки photos(находится в корне вашего проекта)
     *
     * @param user
     * @param chat
     * @return
     */
    @BotRequest(value = "/photos", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest photos(User user, Chat chat) {
        TelegramUser telegramUser = this.telegramUserService.getByChatId(chat.id());
        if (telegramUser.getStep().equals("completed")) {
            this.historyService.add(telegramUser.getChatId(), new History(Action.PHOTOS));
            return sendMessageWithReply(telegramUser.getChatId(), "photos");
        } else {
            return sendMessageWithReply(telegramUser.getChatId(), "Registration uncompleted. To complete registration send /register");
        }
    }


    @BotRequest(value = "{message:[\\S ]+}", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest listen(@BotPathVariable("message") String text, User user, Chat chat) {
        TelegramUser telegramUser = this.telegramUserService.getByChatId(chat.id());
        if (telegramUser.getStep().equals("start")) {
            telegramUser.setLogin(text);
            telegramUser.setStep("login");
            this.telegramUserService.update(telegramUser);
            return sendMessageWithReply(telegramUser.getChatId(), "Enter name:");
        } else if (telegramUser.getStep().equals("login")) {
            telegramUser.setName(text);
            telegramUser.setStep("age");
            this.telegramUserService.update(telegramUser);
            return sendMessageWithReply(telegramUser.getChatId(), "Enter age:");
        } else if (telegramUser.getStep().equals("age")) {
            try {
                telegramUser.setAge(Integer.parseInt(text));
                telegramUser.setStep("completed");
                this.telegramUserService.update(telegramUser);
                return sendMessageWithKeyboard(telegramUser.getChatId(), "Registration completed");
            } catch (NumberFormatException e) {
                return sendMessageWithReply(telegramUser.getChatId(), "Wrong number. Use only numbers. Enter age:");
            }
        } else {
            return sendMessageWithKeyboard(telegramUser.getChatId(), "test");
        }
    }
}
