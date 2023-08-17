//package com.example.backend.TelegramBotsApi.config;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//public class RegisterBot implements CommandLineRunner {
//    @Override
//    public void run(String... args) throws Exception {
//        if (!BotConfig.DOMAIN.startsWith("https://"))
//            throw new RuntimeException("Your domain must be HTTPS");
//        if (BotConfig.DOMAIN.isEmpty())
//            throw new RuntimeException("Please enter your personal domain");
//        if (BotConfig.API_TOKEN.isEmpty())
//            throw new RuntimeException("Please enter your telegram bot token");
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getForObject(BotConfig.API_TELEGRAM + "/setwebhook?url=" + BotConfig.DOMAIN + "/api/v1/bot", Object.class);
//    }
//}
