package com.example.backend.service.settingsService;

import com.example.backend.repository.SettingsRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements SettingsService{
    private final SettingsRepository settingsRepository;

    public SettingsServiceImpl(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }
    @Override
    public HttpEntity<?> getAllSettings(){
        return ResponseEntity.ok(settingsRepository.findAll());
    }
}
