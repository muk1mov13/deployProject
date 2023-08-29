package com.example.backend.service.territoryService;

import com.example.backend.entity.Territory;
import com.example.backend.projection.TerritoryProjection;
import com.example.backend.repository.TerritoryRepository;
import com.example.backend.service.fileService.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TerritoryServiceImplTest {

    @Mock
    private TerritoryRepository territoryRepository;

    @Mock
    private FileService fileService;

    private TerritoryServiceImpl territoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        territoryService = new TerritoryServiceImpl(territoryRepository, fileService);
    }

    @Test
    void getAllTerritoriesForClient_shouldReturnTerritories() {
        // Arrange
        List<TerritoryProjection> territoryProjections = new ArrayList<>();
        TerritoryProjection territoryProjectionMock = mock(TerritoryProjection.class);
        territoryProjections.add(territoryProjectionMock);

        when(territoryRepository.findAllTerritoriesForClient()).thenReturn(territoryProjections);

        // Act
        HttpEntity<?> response = territoryService.getAllTerritoriesForClient();

        // Assert
        assertNotNull(response);
        assertEquals(ResponseEntity.ok(territoryProjections), response);
        verify(territoryRepository).findAllTerritoriesForClient();
    }

    @Test
    void getTerritories_withActiveAndSearch_shouldReturnTerritories() {
        // Arrange
        Integer page = 0;
        Integer size = 10;
        Boolean active = true;
        String search = "example";
        List<Territory> territories = new ArrayList<>();
        Page<Territory> territoryPage = mock(Page.class);
        when(territoryRepository.findAllByActiveAndNameContainsIgnoreCaseOrRegionContainsIgnoreCase(active, search, Pageable.ofSize(size))).thenReturn(territoryPage);
        when(territoryPage.getContent()).thenReturn(territories);

        // Act
        HttpEntity<?> result = territoryService.getTerritories(page, size, active, search);

        // Assert
        assertNotNull(result);
        assertEquals(ResponseEntity.ok(territoryPage), result);
        verify(territoryRepository).findAllByActiveAndNameContainsIgnoreCaseOrRegionContainsIgnoreCase(active, search, Pageable.ofSize(size));
    }

    @Test
    void getTerritories_withSearchAndNoActive_shouldReturnTerritories() {
        // Arrange
        Integer page = 0;
        Integer size = 10;
        String search = "example";
        List<Territory> territories = new ArrayList<>();
        Page<Territory> territoryPage = mock(Page.class);
        when(territoryRepository.findAllByNameContainsIgnoreCaseOrRegionContainsIgnoreCaseOrderByCreatedAtDesc(search, search, Pageable.ofSize(size))).thenReturn(territoryPage);
        when(territoryPage.getContent()).thenReturn(territories);

        // Act
        HttpEntity<?> result = territoryService.getTerritories(page, size, null, search);

        // Assert
        assertNotNull(result);
        assertEquals(ResponseEntity.ok(territoryPage), result);
        verify(territoryRepository).findAllByNameContainsIgnoreCaseOrRegionContainsIgnoreCaseOrderByCreatedAtDesc(search, search, Pageable.ofSize(size));
    }

    @Test
    void saveTerritory_shouldSaveTerritory() {
        UUID id = UUID.randomUUID();
        // Arrange
        Territory territory = Territory
                .builder()
                .id(id)
                .name("test")
                .region("region")
                .active(true)
                .Long(45.45)
                .Lat(54.2)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        Territory savedTerritory = Territory
                .builder()
                .id(id)
                .name("test")
                .region("region")
                .active(true)
                .Long(45.45)
                .Lat(54.2)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        ;
        when(territoryRepository.save(territory)).thenReturn(savedTerritory);

        // Act
        HttpEntity<?> result = territoryService.saveTerritory(territory);

        // Assert
        assertNotNull(result);
        assertEquals(ResponseEntity.ok(savedTerritory), result);
        assertNotNull(savedTerritory.getCreatedAt());
        verify(territoryRepository).save(territory);
    }


    @Test
    void getExcelFile_shouldReturnExcelFile() {
        // Arrange
        boolean active = true;
        String search = "example";
        List<String> columnNames = Collections.singletonList("name");
        Page<Territory> territories = mock(Page.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(territoryRepository.findAllByActiveAndNameContainsIgnoreCaseOrRegionContainsIgnoreCase(active, search, Pageable.unpaged())).thenReturn(territories);
        when(fileService.createExcelFile(territories.getContent(), columnNames, Territory.class, false)).thenReturn(ResponseEntity.ok().build());

        // Act
        HttpEntity<?> result = territoryService.getExcelFile(active, search, columnNames, response);

        // Assert
        assertNotNull(result);
        assertEquals(ResponseEntity.ok().build(), result);
        verify(territoryRepository).findAllByActiveAndNameContainsIgnoreCaseOrRegionContainsIgnoreCase(active, search, Pageable.unpaged());
        verify(fileService).createExcelFile(territories.getContent(), columnNames, Territory.class, false);
    }

    // Add more test methods for other public methods in TerritoryServiceImpl

    @Test
    void updateTerritory_withValidId_shouldUpdateTerritory() {
        // Arrange
        UUID id = UUID.randomUUID();
        Territory territoryToUpdate = new Territory();
        territoryToUpdate.setName("Updated Territory");
        territoryToUpdate.setRegion("Updated Region");
        territoryToUpdate.setActive(true);
        territoryToUpdate.setLong(2.0);
        territoryToUpdate.setLat(1.0);
        Territory existingTerritory = new Territory();
        existingTerritory.setId(id);
        when(territoryRepository.findById(id)).thenReturn(java.util.Optional.of(existingTerritory));
        when(territoryRepository.save(existingTerritory)).thenReturn(existingTerritory);

        // Act
        HttpEntity<?> result = territoryService.updateTerritory(String.valueOf(id), territoryToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals(ResponseEntity.ok(existingTerritory), result);
        assertEquals("Updated Territory", existingTerritory.getName());
        assertEquals("Updated Region", existingTerritory.getRegion());
        assertTrue(existingTerritory.isActive());
        assertEquals(2.0, existingTerritory.getLong());
        assertEquals(1.0, existingTerritory.getLat());
    }

    // Add more test methods for other public methods in TerritoryServiceImpl
}