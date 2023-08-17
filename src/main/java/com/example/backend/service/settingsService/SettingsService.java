package com.example.backend.service.settingsService;

import org.springframework.http.HttpEntity;

public interface SettingsService {
    HttpEntity<?> getAllSettings();
}

