package com.dmitriikuzmin.service;

import org.springframework.web.multipart.MultipartFile;

public interface TelegramService {

    void sendMessage(long chatId, String message);

    void sendImage(long chatId, MultipartFile file);

    void sendDocument(long chatId, MultipartFile file);


}
