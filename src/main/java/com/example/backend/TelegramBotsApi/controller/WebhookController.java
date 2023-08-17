package com.example.backend.TelegramBotsApi.controller;

import com.example.backend.Mybot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("/api/tujjor_bot/public")
@RequiredArgsConstructor
public class WebhookController {

    final Mybot mybot;

    @PostMapping
    public void onUpdateReceived(@RequestBody Update update){
        mybot.onUpdateReceived(update);
    }

}
