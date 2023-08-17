package com.example.backend.TelegramBotsApi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class From {
    private Long id;
    @JsonProperty(value = "is_bot")
    private Boolean isBot;
    @JsonProperty(value = "first_name")
    private String firstName;
    private String username;
}
