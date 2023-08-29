package com.example.backend.repository;

import com.example.backend.entity.Territory;
import com.example.backend.projection.TerritoryProjection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TerritoryRepositoryTest {

    @Autowired
    private TerritoryRepository territoryRepository;

    @BeforeEach
    public void setUp() {
        Territory testTerritory = Territory
                .builder()
                .id(UUID.randomUUID())
                .name("test")
                .region("test")
                .active(true)
                .Long(21254.21)
                .Lat(65454.15)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        territoryRepository.save(testTerritory);
    }

    @Test
    public void testFindAllTerritoriesForClient() {
        // Call the findAllTerritoriesForClient method
        List<TerritoryProjection> territories = territoryRepository.findAllTerritoriesForClient();

        // Assert that the returned list is not empty
        Assertions.assertFalse(territories.isEmpty());
    }

    @Test
    public void testFindAllTerritoriesForBot() {
        // Create a sample pageable object
        Pageable pageable = PageRequest.of(0, 10);
        // Call the findAllTerritoriesForBot method
        Page<Territory> territories = territoryRepository.findAllTerritoriesForBot(pageable);
        // Assert that the returned page contains territories
        Assertions.assertFalse(territories.isEmpty());
    }

    @Test
    public void testFindAllByNameContainsIgnoreCaseOrRegionContainsIgnoreCaseOrderByCreatedAtDesc() {
        // Create sample territories
        Territory territory1 = Territory
                .builder()
                .id(UUID.randomUUID())
                .name("Test Territory")
                .region("region 1")
                .active(true)
                .Long(21212154.21)
                .Lat(6556454.15)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        Territory territory2 = Territory
                .builder()
                .id(UUID.randomUUID())
                .name("Another Territory")
                .region("Region 2")
                .active(false)
                .Long(21445254.21)
                .Lat(6541154.15)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        // Save the territories to the repository
        territoryRepository.save(territory1);
        territoryRepository.save(territory2);

        // Create a sample pageable object
        Pageable pageable = PageRequest.of(0, 10);

        // Call the findAllByNameContainsIgnoreCaseOrRegionContainsIgnoreCaseOrderByCreatedAtDesc method
        Page<Territory> territories = territoryRepository.findAllByNameContainsIgnoreCaseOrRegionContainsIgnoreCaseOrderByCreatedAtDesc(
                "Territory", "Region", pageable);

        // Assert that the page contains the expected territories
        Assertions.assertEquals("Test Territory", territories.getContent().get(0).getName());
        Assertions.assertEquals("Another Territory", territories.getContent().get(1).getName());
    }

    @Test
    public void testFindAllByActiveAndNameContainsIgnoreCaseOrRegionContainsIgnoreCase() {
        // Create a sample pageable object
        Pageable pageable = PageRequest.of(0, 10);

        // Call the findAllByActiveAndNameContainsIgnoreCaseOrRegionContainsIgnoreCase method
        Page<Territory> territories = territoryRepository.findAllByActiveAndNameContainsIgnoreCaseOrRegionContainsIgnoreCase(
                true, "", pageable);

        // Assert that the returned page is not empty
        Assertions.assertFalse(territories.isEmpty());
    }

    @Test
    public void testFindByName() {
        // Call the findByName method
        Optional<Territory> foundTerritoryOptional = territoryRepository.findByName("test");

        // Assert that the territory was found
        Assertions.assertTrue(foundTerritoryOptional.isPresent());
        Territory foundTerritory = foundTerritoryOptional.get();
        Assertions.assertEquals("test", foundTerritory.getName());
    }
}

