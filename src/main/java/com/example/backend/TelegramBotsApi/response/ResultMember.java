package com.example.backend.TelegramBotsApi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResultMember {
    private Member user = new Member();
    private String status;
    @JsonProperty(value = "is_anonymous")
    private Boolean isAnonymous;
}
