
package com.example.backend.controller;


import com.example.backend.entity.Territory;
import com.example.backend.service.territoryService.TerritoryService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/territory")
@RequiredArgsConstructor
public class TerritoryController {

    private final TerritoryService territoryService;

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/all")
    public HttpEntity<?> getAllTerritories() {
        return territoryService.getAllTerritoriesForClient();
    }


    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping
    public HttpEntity<?> getTerritories(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "") Boolean active,
            @RequestParam(defaultValue = "") String search
            ) {
        return territoryService.getTerritories(page, size, active, search);
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/map")
    public HttpEntity<?> getAllTerritoriesForMap() {
        return territoryService.getAllTerritoriesForMap();
    }



    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @PostMapping
    public HttpEntity<?> saveTerritory(@RequestBody Territory territory) {
        return territoryService.saveTerritory(territory);
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @PutMapping("{id}")
    public HttpEntity<?> updateTerritory(@PathVariable String id, @RequestBody Territory territory) {
        return territoryService.updateTerritory(id, territory);
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/excel")
    public HttpEntity<?> getExcelFile(@RequestParam(defaultValue = "") Boolean active,
                                      @RequestParam(defaultValue = "") String search,
                                      @RequestParam(defaultValue = "id") List<String> columnNames,
                                      HttpServletResponse response) {
        return territoryService.getExcelFile(active,
                search,
                columnNames,
                response);
    }
}

