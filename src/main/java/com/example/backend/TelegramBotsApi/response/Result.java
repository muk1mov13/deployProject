package com.example.backend.TelegramBotsApi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Result {
    @JsonProperty(value = "message_id")
    private Integer messageId;
    private From from = new From();
    private Chat chat = new Chat();
    private Date date;
}
