package com.example.backend.TelegramBotsApi.services.Execute;

import com.example.backend.TelegramBotsApi.response.ChatMember;
import com.example.backend.TelegramBotsApi.response.Message;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface Execute {
    Message send(SendMessage sendMessage);

    Message send(SendPhoto sendPhoto);

    Message send(SendVideo sendVideo);

    void send(DeleteMessage deleteMessage);

    Message send(EditMessageText editMessageText);

    Message send(EditMessageCaption editMessageCaption);

    ChatMember send(GetChatMember getChatMember);
}
