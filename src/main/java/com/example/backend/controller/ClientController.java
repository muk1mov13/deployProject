package com.example.backend.controller;

import com.example.backend.payload.request.ReqClientSave;
import com.example.backend.service.clientService.ClientService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/filter")
    public HttpEntity<?> getClientsByFilter(@RequestParam(defaultValue = "") Boolean active,
                                    @RequestParam(defaultValue = "") String location,
                                    @RequestParam(defaultValue = "") String  cities,
                                    @RequestParam(defaultValue = "") String  days,
                                    @RequestParam(defaultValue = "") Boolean tin,
                                    @RequestParam(defaultValue = "" ) String  categories,
                                    @RequestParam(defaultValue = "") String inventory,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10")  int size,
                                      @RequestParam(defaultValue = "") String search
                              ){
        return clientService.getClientsByFilter( active,
                location,
                cities.isEmpty()?List.of():Arrays.stream(cities.split(","))
                        .map(UUID::fromString)
                        .collect(Collectors.toList()),
                days.isEmpty()?List.of():Arrays.stream(days.split(","))
                        .collect(Collectors.toList()),
                tin,
                categories.isEmpty()?List.of():Arrays.stream(categories.split(","))
                        .map(UUID::fromString)
                        .collect(Collectors.toList()),
                inventory,
                page,
                size,search);
    }
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @PostMapping
    public HttpEntity<?> saveNewClient(@RequestBody ReqClientSave newClient){
        return clientService.saveNewClient(newClient);
    }
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @PutMapping
    public HttpEntity<?> editNewClient(@RequestBody ReqClientSave newClient) throws Exception {
        return clientService.editClientData(newClient);
    }
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/excel")
    public HttpEntity<?> getExcelFile(@RequestParam(defaultValue = "") Boolean active,
                                      @RequestParam(defaultValue = "") String location,
                                      @RequestParam(defaultValue = "") String cities,
                                      @RequestParam(defaultValue = "") String days,
                                      @RequestParam(defaultValue = "") Boolean tin,
                                      @RequestParam(defaultValue = "" ) String categories,
                                      @RequestParam(defaultValue = "") String inventory,
                                      @RequestParam(defaultValue = "") String search,
                                      @RequestParam(defaultValue = "id") List<String> columnNames,
                                      HttpServletResponse response) {
        return clientService.getExcelFile(active,
                location,
                cities.isEmpty()?List.of():Arrays.stream(cities.split(","))
                        .map(UUID::fromString)
                        .collect(Collectors.toList()),
                days.isEmpty()?List.of():Arrays.stream(days.split(","))
                        .collect(Collectors.toList()),
                tin,
                categories.isEmpty()?List.of():Arrays.stream(categories.split(","))
                        .map(UUID::fromString)
                        .collect(Collectors.toList()),
                inventory,
                search,
                columnNames,
                response);
    }
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/map")
    public HttpEntity<?> getClientsForMap(){
            return clientService.getClientsForMap();
    }

}
