package com.example.backend.TelegramBotsApi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Chat {
    @JsonProperty(value = "id")
    private Long chatId;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
    private String username;
    private String type;
}
