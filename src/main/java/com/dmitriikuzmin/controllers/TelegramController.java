package com.dmitriikuzmin.controllers;

import com.dmitriikuzmin.dto.ResponseResult;
import com.dmitriikuzmin.model.TelegramUser;
import com.dmitriikuzmin.service.TelegramService;
import com.dmitriikuzmin.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/telegram")
public class TelegramController {
    TelegramService telegramService;
    TelegramUserService telegramUserService;

    @Autowired
    public void setTelegramService(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Autowired
    public void setTelegramUserService(TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
    }

    @PostMapping("/message/{chatId}")
    public ResponseEntity<ResponseResult<TelegramUser>> send(@PathVariable long chatId, @RequestParam String message) {
        try {
            this.telegramService.sendMessage(chatId, message);
            return new ResponseEntity<>(new ResponseResult<>(null, this.telegramUserService.getByChatId(chatId)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/image/{chatId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void sendImage(@PathVariable long chatId, @RequestPart MultipartFile file) {
        this.telegramService.sendImage(chatId, file);
    }

    @PostMapping(value = "/document/{chatId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseResult<TelegramUser>> sendDocument(@PathVariable long chatId, @RequestPart MultipartFile file) {
        try {
            this.telegramService.sendDocument(chatId, file);
            return new ResponseEntity<>(new ResponseResult<>(null, this.telegramUserService.getByChatId(chatId)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseResult<List<TelegramUser>>> getAll() {
        return new ResponseEntity<>(new ResponseResult<>(null, this.telegramUserService.getAll()), HttpStatus.OK);
    }
}
