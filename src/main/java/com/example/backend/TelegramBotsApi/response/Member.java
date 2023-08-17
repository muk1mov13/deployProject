package com.example.backend.TelegramBotsApi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Member {
    private Long id;
    @JsonProperty(value = "is_bot")
    private Boolean isBot;
    @JsonProperty(value = "first_name")
    private String firstName;
    private String username;
    @JsonProperty(value = "language_code")
    private String language;
}
