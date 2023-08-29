package com.example.backend.repository;

import com.example.backend.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TelegramUserRepositoryTest {

    @Autowired
    private TelegramUserRepository telegramUserRepository;

    @Test
    public void testFindByChatId() {
        // Create a sample TelegramUser
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(123456L); // Set the chatId

        // Save the TelegramUser to the repository
        telegramUserRepository.save(telegramUser);

        // Call the findByChatId method
        Optional<TelegramUser> foundUserOptional = telegramUserRepository.findByChatId(123456L);

        // Assert that the user was found and the chatId matches
        Assertions.assertTrue(foundUserOptional.isPresent());
        TelegramUser foundUser = foundUserOptional.get();
        Assertions.assertEquals(123456L, foundUser.getChatId());
    }
}