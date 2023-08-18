




package com.example.backend.service.territoryService;

import com.example.backend.entity.Territory;
import com.example.backend.repository.TerritoryRepository;
import com.example.backend.service.fileService.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class TerritoryServiceImpl implements TerritoryService {

    private final TerritoryRepository territoryRepository;
    private final FileService fileService;

    public TerritoryServiceImpl(TerritoryRepository territoryRepository, FileService fileService) {
        this.territoryRepository = territoryRepository;
        this.fileService = fileService;
    }
    @Override
    public HttpEntity<?> getAllTerritoriesForClient(){
        return ResponseEntity.ok(territoryRepository.findAllTerritoriesForClient());
    }

    @Override
    public HttpEntity<?> getExcelFile(Boolean active, String search, List<String> columnNames, HttpServletResponse response) {
        Pageable pageable = Pageable.unpaged();
        Page<Territory> filteredTerritories = territoryRepository.findAllByActiveAndNameContainsIgnoreCaseOrRegionContainsIgnoreCase(active, search, pageable);
        System.out.println(filteredTerritories.getContent());
        return fileService.createExcelFile(filteredTerritories.getContent(),columnNames,Territory.class,false);
    }


//    @Override
//    public List<Territory> getTerritoriesActive() {
//        return territoryRepository.getTerritoriesActive().orElse(Collections.emptyList());
//    }
//
//    @Override
//    public HttpEntity<?> getTerritoriesByActive(boolean active) {
//        List<Territory> territories = null;
//        if (active) {
//            territories = territoryRepository.getTerritoriesByActive(true).orElse(Collections.emptyList());
//        } else {
//            territories = territoryRepository.getTerritoriesByActive(false).orElse(Collections.emptyList());
//        }
//        return ResponseEntity.ok(territories);
//    }

    @Override
    public HttpEntity<?> getTerritories(Integer page, Integer size, Boolean active, String search) {
        Pageable pageable = size==-1 ? Pageable.unpaged() : PageRequest.of(page > 0 ? page - 1 : 0, size);
        // Check if search is provided but active is null, then search by name or region
        if (active == null) {
            Page<Territory> searchedTerritories = territoryRepository.findAllByNameContainsIgnoreCaseOrRegionContainsIgnoreCaseOrderByCreatedAtDesc(search, search, pageable);
            return ResponseEntity.ok(searchedTerritories);
        }
        // Check if active is provided, then search by active and name or region
        Page<Territory> activeSearchedTerritories = territoryRepository.findAllByActiveAndNameContainsIgnoreCaseOrRegionContainsIgnoreCase(active, search, pageable);
        return ResponseEntity.ok(activeSearchedTerritories);
    }

    @Override
    public HttpEntity<?> saveTerritory(Territory territory) {
        territory.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        Territory savedTerritory = territoryRepository.save(territory);
        System.out.println(savedTerritory);
        return ResponseEntity.ok(savedTerritory);
    }

    @Override
    public HttpEntity<?> updateTerritory(String id, Territory territory) {
        Territory willBeUpdatedTerritory = territoryRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NoSuchElementException("not found"));
        willBeUpdatedTerritory.setName(territory.getName());
        willBeUpdatedTerritory.setRegion(territory.getRegion());
        willBeUpdatedTerritory.setActive(territory.isActive());
        willBeUpdatedTerritory.setLong(territory.getLong());
        willBeUpdatedTerritory.setLat(territory.getLat());
        Territory updatedTerritory = territoryRepository.save(willBeUpdatedTerritory);
        return ResponseEntity.ok(updatedTerritory);
    }

}

