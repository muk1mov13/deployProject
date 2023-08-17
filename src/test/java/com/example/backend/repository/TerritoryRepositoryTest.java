package com.example.backend.repository;

import com.example.backend.entity.Territory;
import com.example.backend.service.territoryService.TerritoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TerritoryRepositoryTest {

    @Autowired
    private TerritoryRepository territoryRepository;


    @Test
    public void testFindAllByNameOrRegionIgnoreCase() {
        UUID uuidA = UUID.randomUUID();
        UUID uuidB = UUID.randomUUID();
        UUID uuidC = UUID.randomUUID();
        UUID uuidD = UUID.randomUUID();
        // Mock data
        List<Territory> mockTerritories = new ArrayList<>();
        mockTerritories.add(new Territory(uuidA, "Territory A", "Region X", true, null, null, Timestamp.valueOf(LocalDateTime.now())));
        mockTerritories.add(new Territory(uuidB, "Territory B", "Region Y", true, null, null,Timestamp.valueOf(LocalDateTime.now())));
        mockTerritories.add(new Territory(uuidC, "Territory C", "Region W", true, null, null,Timestamp.valueOf(LocalDateTime.now())));
        mockTerritories.add(new Territory(uuidC, "Territory D", "Region Z", true, null, null,Timestamp.valueOf(LocalDateTime.now())));

        // Mocking the repository behavior
        Pageable pageable = (Pageable) PageRequest.of(0, 2);
        when(territoryRepository.findAllByNameContainsIgnoreCaseOrRegionContainsIgnoreCaseOrderByCreatedAtDesc(anyString(), anyString(), eq(pageable)))
                .thenAnswer(invocation -> {
                    String name = invocation.getArgument(0);
                    String region = invocation.getArgument(1);
                    List<Territory> filteredTerritories = mockTerritories.stream()
                            .filter(territory -> territory.getName().toLowerCase().contains(name.toLowerCase())
                                    || territory.getRegion().toLowerCase().contains(region.toLowerCase()))
                            .collect(Collectors.toList());

                    return new PageImpl<>(filteredTerritories, (org.springframework.data.domain.Pageable) pageable, filteredTerritories.size());
                });

        // Test the service method
        Page<Territory> resultPage = territoryRepository.findAllByNameContainsIgnoreCaseOrRegionContainsIgnoreCaseOrderByCreatedAtDesc("Territory", "Region", (org.springframework.data.domain.Pageable) pageable);

        // Assert the result
        Assertions.assertEquals(4, resultPage.getTotalElements());
        Assertions.assertEquals(uuidA, resultPage.getContent().get(0).getId());
        Assertions.assertEquals(uuidB, resultPage.getContent().get(1).getId());
        Assertions.assertEquals(uuidC, resultPage.getContent().get(2).getId());

        // Verify the repository method was called with correct arguments
        verify(territoryRepository, times(1)).findAllByNameContainsIgnoreCaseOrRegionContainsIgnoreCaseOrderByCreatedAtDesc("Territory", "Region", (org.springframework.data.domain.Pageable) pageable);
    }
}

