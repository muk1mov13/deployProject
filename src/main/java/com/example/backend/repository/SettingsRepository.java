package com.example.backend.repository;

import com.example.backend.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SettingsRepository extends JpaRepository<Settings, UUID> {
}
