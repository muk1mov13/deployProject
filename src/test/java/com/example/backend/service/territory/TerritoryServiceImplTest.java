package com.example.backend.service.territory;

import com.example.backend.entity.Territory;
import com.example.backend.repository.TerritoryRepository;
import com.example.backend.service.fileService.FileService;
import com.example.backend.service.territoryService.TerritoryServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TerritoryServiceImplTest {

//    By using lenient = true, Mockito will not raise the UnnecessaryStubbingException for that specific stubbing, and you can retain it in your test code without causing any issues.

    @Mock
    private TerritoryRepository territoryRepository;

    @InjectMocks
    private TerritoryServiceImpl territoryService;
    @InjectMocks
    private FileService fileService;

    @Before
    public void setup() {
        // Initialize mocks manually
        MockitoAnnotations.initMocks(this);
        territoryService = new TerritoryServiceImpl(territoryRepository,fileService);
    }



    @Test
    public void testSaveTerritory_Success() {
        // Create a new Territory object to save
        Territory territoryToSave = Territory.builder()
                .name("city")
                .region("street")
                .active(true)
                .Long(null)
                .Lat(null)
                .build();
        // Add more properties as needed

        // Create a mock of the saved Territory object
        Territory savedTerritory = Territory.builder()
                .id(UUID.randomUUID())
                .name("city")
                .region("street")
                .active(true)
                .Long(null)
                .Lat(null)
                .build();
        // Add more properties as needed

        // Mock the territoryRepository.save method to return the mock savedTerritory
        when(territoryRepository.save(any(Territory.class))).thenReturn(savedTerritory);

        // Call the method under test
        HttpEntity<?> response = territoryService.saveTerritory(territoryToSave);

        // Verify that the territoryRepository.save method was called with the correct argument
        verify(territoryRepository).save(territoryToSave);

        // You can further assert the contents of the response if needed
        // For example, if the response contains the saved territory, you can assert its properties
        // Assuming the response contains the saved territory as a JSON representation:
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Territory);
        Territory responseBody = (Territory) response.getBody();
        assertEquals(savedTerritory.getId(), responseBody.getId());
        assertEquals(savedTerritory.getName(), responseBody.getName());
        assertEquals(savedTerritory.getRegion(), responseBody.getRegion());
        // Add more assertions for other properties if needed
    }


    @Test
    public void testUpdateTerritory_Success() {
        // Create a sample territory ID and a territory object to update
        String territoryId = "d3a35c3e-3b7a-4c02-8b34-1cde96c90b63"; // Replace with a valid territory ID
        Territory updatedTerritory = new Territory();
        updatedTerritory.setName("Updated Territory");
        updatedTerritory.setRegion("Updated Region");
        updatedTerritory.setActive(true);
        updatedTerritory.setLong(100.0);
        updatedTerritory.setLat(50.0);

        // Create a mock of the territory to be updated
        Territory existingTerritory = new Territory();
        existingTerritory.setId(UUID.fromString(territoryId));
        existingTerritory.setName("Territory 1");
        existingTerritory.setRegion("Region 1");
        existingTerritory.setActive(false);
        existingTerritory.setLong(80.0);
        existingTerritory.setLat(40.0);

        // Mock the territoryRepository.findById method to return the mock existingTerritory
        when(territoryRepository.findById(UUID.fromString(territoryId))).thenReturn(Optional.of(existingTerritory));

        // Mock the territoryRepository.save method to return the mock updatedTerritory
        when(territoryRepository.save(any(Territory.class))).thenReturn(updatedTerritory);

        // Call the method under test
        ResponseEntity<Territory> response = (ResponseEntity<Territory>) territoryService.updateTerritory(territoryId, updatedTerritory);

        // Verify that the territoryRepository.findById and territoryRepository.save methods were called
        verify(territoryRepository).findById(UUID.fromString(territoryId));
        verify(territoryRepository).save(existingTerritory);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // You can further assert the contents of the response if needed
        // For example, if the response contains the updated territory, you can assert its properties
        // Assuming the response contains the updated territory as a JSON representation:
        Territory responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(updatedTerritory, responseBody);
        // Add more assertions for other properties if needed
    }

}
