package com.example.backend.TelegramBotsApi.services.Execute;

import com.example.backend.TelegramBotsApi.config.BotConfig;
import com.example.backend.TelegramBotsApi.response.ChatMember;
import com.example.backend.TelegramBotsApi.response.Message;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ExecuteImpl implements Execute {

    /**
     * SendMessage Method
     */
    @Override
    public Message send(SendMessage sendMessage) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(BotConfig.API_TELEGRAM + "/sendMessage", sendMessage, Message.class);
    }

    /**
     * SendPhoto Method
     */
    @SneakyThrows
    @Override
    public Message send(SendPhoto sendPhoto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Path path = Paths.get(sendPhoto.getPhoto().getAttachName());
        byte[] photoBytes = Files.readAllBytes(path);
        ByteArrayResource photo = new ByteArrayResource(photoBytes) {
            @Override
            public String getFilename() {
                return path.getFileName().toString();
            }
        };
        body.add("chat_id", sendPhoto.getChatId());
        body.add("photo", photo);
        body.add("message_thread_id", sendPhoto.getMessageThreadId());
        body.add("caption", sendPhoto.getCaption());
        body.add("parse_mode", sendPhoto.getParseMode());
        body.add("caption_entities", sendPhoto.getCaptionEntities());
        body.add("has_spoiler", sendPhoto.getHasSpoiler());
        body.add("disable_notification", sendPhoto.getDisableNotification());
        body.add("protect_content", sendPhoto.getProtectContent());
        body.add("reply_to_message_id", sendPhoto.getReplyToMessageId());
        body.add("allow_sending_without_reply", sendPhoto.getAllowSendingWithoutReply());
        body.add("reply_markup", sendPhoto.getReplyMarkup());
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Message> responseEntity = restTemplate.postForEntity(BotConfig.API_TELEGRAM + "/sendPhoto", request, Message.class);
        return responseEntity.getBody();
    }

    /**
     * SendVideo Method
     */
    @SneakyThrows
    @Override
    public Message send(SendVideo sendVideo) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Path path = Paths.get(sendVideo.getVideo().getAttachName());
        if (sendVideo.getThumbnail() != null) {
            Path pathThumb = Paths.get(sendVideo.getThumbnail().getAttachName());
            byte[] bytesThumb = Files.readAllBytes(pathThumb);
            ByteArrayResource thumb = new ByteArrayResource(bytesThumb) {
                @Override
                public String getFilename() {
                    return pathThumb.getFileName().toString();
                }
            };
            body.add("thumbnail", thumb);
        }
        byte[] bytes = Files.readAllBytes(path);
        ByteArrayResource video = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return path.getFileName().toString();
            }
        };

        body.add("chat_id", sendVideo.getChatId());
        body.add("message_thread_id", sendVideo.getMessageThreadId());
        body.add("video", video);
        body.add("duration", sendVideo.getDuration());
        body.add("width", sendVideo.getWidth());
        body.add("height", sendVideo.getHeight());
        body.add("caption", sendVideo.getCaption());
        body.add("parse_mode", sendVideo.getParseMode());
        body.add("caption_entities", sendVideo.getCaptionEntities());
        body.add("has_spoiler", sendVideo.getHasSpoiler());
        body.add("supports_streaming", sendVideo.getSupportsStreaming());
        body.add("disable_notification", sendVideo.getDisableNotification());
        body.add("protect_content", sendVideo.getProtectContent());
        body.add("reply_to_message_id", sendVideo.getReplyToMessageId());
        body.add("allow_sending_without_reply", sendVideo.getAllowSendingWithoutReply());
        body.add("reply_markup", sendVideo.getReplyMarkup());
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Message> responseEntity = restTemplate.postForEntity(BotConfig.API_TELEGRAM + "/sendVideo", request, Message.class);
        return responseEntity.getBody();
    }

    /**
     * DeleteMessage Method
     */
    @Override
    public void send(DeleteMessage deleteMessage) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(BotConfig.API_TELEGRAM + "/deleteMessage", deleteMessage, Object.class);
    }

    /**
     * EditMessageText Method
     */
    @Override
    public Message send(EditMessageText editMessageText) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(BotConfig.API_TELEGRAM + "/editMessageText", editMessageText, Message.class);
    }

    /**
     * EditMessageCaption
     */
    @Override
    public Message send(EditMessageCaption editMessageCaption) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(BotConfig.API_TELEGRAM + "/editMessageCaption", editMessageCaption, Message.class);
    }

    /**
     * GetChatMember
     */
    @Override
    public ChatMember send(GetChatMember getChatMember) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(BotConfig.API_TELEGRAM + "/getChatMember", getChatMember, ChatMember.class);
    }
}
