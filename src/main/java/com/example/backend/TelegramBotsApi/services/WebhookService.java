package com.example.backend.TelegramBotsApi.services;


import com.example.backend.TelegramBotsApi.response.Message;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
public class WebhookService {
    private final String token = "6137245232:AAE-D41wsTMY6O8ZOTtORyCFt_vutW_SBaw";

    public Message sendExecute(SendMessage sendMessage) {
        HttpEntity<SendMessage> request = new HttpEntity<>(sendMessage);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("https://api.telegram.org/bot" + token + "/sendMessage", request, Message.class);
    }

    public Message updateExecute(EditMessageText editMessageText) {
        HttpEntity<EditMessageText> request = new HttpEntity<>(editMessageText);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("https://api.telegram.org/bot" + token + "/editMessageText", request, Message.class);
    }

    public Message updateExecuteReplyMarkup(EditMessageReplyMarkup editMessageReplyMarkup) {
        HttpEntity<EditMessageReplyMarkup> request = new HttpEntity<>(editMessageReplyMarkup);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("https://api.telegram.org/bot" + token + "/editMessageReplyMarkup", request, Message.class);
    }

    public Message deleteExecute(DeleteMessage deleteMessage) {
        HttpEntity<DeleteMessage> request = new HttpEntity<>(deleteMessage);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("https://api.telegram.org/bot" + token + "/deleteMessage", request, Message.class);
    }


}



