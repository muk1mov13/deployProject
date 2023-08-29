package com.example.backend.service.settingsService;
import com.example.backend.entity.Settings;
import com.example.backend.repository.SettingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SettingsServiceImplTest {
    @Mock
    private SettingsRepository settingsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSettings() {
        List<Settings> mockedSettings = new ArrayList<>();
        // Add some mocked settings to the list

        // Mock the behavior of the settingsRepository
        when(settingsRepository.findAll()).thenReturn(mockedSettings);

        // Create an instance of the SettingsServiceImpl class
        SettingsServiceImpl settingsService = new SettingsServiceImpl(settingsRepository);

        // Call the method under test
        HttpEntity<?> result = settingsService.getAllSettings();

        // Verify the expected behavior
        assertEquals(ResponseEntity.ok(mockedSettings), result);
        verify(settingsRepository, times(1)).findAll();
        verifyNoMoreInteractions(settingsRepository);
    }
}