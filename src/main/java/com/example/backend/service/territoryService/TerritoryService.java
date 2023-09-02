
package com.example.backend.service.territoryService;

import com.example.backend.entity.Territory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;

import java.util.List;

import com.example.backend.entity.Territory;
import org.springframework.http.HttpEntity;

import java.util.List;

public interface TerritoryService {

    HttpEntity<?> getAllTerritoriesForClient();

    //    repositoryga kelgan paramlarga qarab parametr qabul qiladi
    HttpEntity<?> getTerritories(Integer page, Integer size, Boolean active, String search);

    HttpEntity<?> saveTerritory(Territory territory);

    HttpEntity<?> updateTerritory(String id, Territory territory);

    HttpEntity<?> getExcelFile(Boolean active, String search, List<String> columnNames, HttpServletResponse response);


}

