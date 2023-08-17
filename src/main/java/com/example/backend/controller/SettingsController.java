package com.example.backend.controller;
import com.example.backend.service.settingsService.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {
    private final SettingsService settingsService;
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping
    public HttpEntity<?> getAllSettings(){
        return settingsService.getAllSettings();
    }
}
