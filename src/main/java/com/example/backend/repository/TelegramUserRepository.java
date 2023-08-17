package com.example.backend.repository;

import com.example.backend.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, UUID> {

    @Query(value = "select * from telegram_user where chat_id=:chatId", nativeQuery = true)
    Optional<TelegramUser> findByChatId(Long chatId);
}
