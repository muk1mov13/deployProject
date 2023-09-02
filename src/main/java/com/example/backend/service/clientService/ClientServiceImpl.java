package com.example.backend.service.clientService;

import com.example.backend.constants.Day;
import com.example.backend.entity.Client;
import com.example.backend.entity.ClientPlan;
import com.example.backend.payload.request.ReqClientSave;
import com.example.backend.projection.ClientProjection;
import com.example.backend.repository.ClientPlanRepository;
import com.example.backend.repository.ClientRepository;
import com.example.backend.repository.CustomerCategoryRepository;
import com.example.backend.repository.TerritoryRepository;
import com.example.backend.service.fileService.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final CustomerCategoryRepository customerCategoryRepository;
    private final TerritoryRepository territoryRepository;
    private final FileService fileService;

    private final ClientPlanRepository clientPlanRepository;

    public ClientServiceImpl(ClientRepository clientRepository, CustomerCategoryRepository customerCategoryRepository, TerritoryRepository territoryRepository, FileService fileService, ClientPlanRepository clientPlanRepository) {
        this.clientRepository = clientRepository;
        this.customerCategoryRepository = customerCategoryRepository;
        this.territoryRepository = territoryRepository;
        this.fileService = fileService;
        this.clientPlanRepository = clientPlanRepository;
    }

    @Override
    public HttpEntity<?> getClientsByFilter(Boolean active,
                                            String location,
                                            List<UUID> cities,
                                            List<String> days,
                                            Boolean tin,
                                            List<UUID> categories,
                                            String inventory,
                                            int pageNumber,
                                            int pageSize,
                                            String search
    ) {

        Pageable pageable = pageSize == -1 ? Pageable.unpaged() : PageRequest.of(pageNumber > 0 ? pageNumber - 1 : 0, pageSize);
        return ResponseEntity.ok(clientRepository.findClientsByFilter(active, cities, tin, categories, search, pageable));
    }


    @Override
    public HttpEntity<?> getExcelFile(Boolean active,
                                      String location,
                                      List<UUID> cities,
                                      List<String> days,
                                      Boolean tin,
                                      List<UUID> categories,
                                      String inventory,
                                      String search,
                                      List<String> columnNames,
                                      HttpServletResponse response) {
        Pageable pageable = Pageable.unpaged();
        Page<ClientProjection> filteredClients = clientRepository.findClientsByFilter(active, cities, tin, categories, search, pageable);
        return fileService.createExcelFile(filteredClients.getContent(), columnNames, Client.class, true);
    }

    @Override
    public HttpEntity<?> saveNewClient(ReqClientSave newClient) {
        System.out.println(newClient.getTin());

        Client save = clientRepository.save(new Client(newClient.getName(),
                newClient.getAddress(),
                newClient.getTelephone(),
                Objects.equals(newClient.getTin(), "") ? null : newClient.getTin(),
                newClient.isActive(),
                newClient.getCompanyName(),
                customerCategoryRepository.findById(newClient.getCategoryId()).get(),
                territoryRepository.findById(newClient.getTerritoryId()).get(),
                newClient.getLongitude(),
                newClient.getLatitude(),
                null, // this section should be changed in the next milestones
                newClient.getReferencePoint(),
                Timestamp.valueOf(LocalDateTime.now())
        ));
        clientPlanRepository.save(ClientPlan
                .builder()
                .id(UUID.randomUUID())
                .amount(600)
                .client(save)
                .localDate(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .build()
        );
        return ResponseEntity.ok(save);
    }

    public String[] filterArrayOfDays(String[] strArray) {
        Set<String> daysSet = new HashSet<>();
        for (String s : strArray) {
            if (isValidDay(s)) {
                daysSet.add(s);
            }
        }
        return daysSet.toArray(new String[0]);
    }

    public boolean isValidDay(String str) {
        try {
            Day.valueOf(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public HttpEntity<?> getClientsForMap() {
        return ResponseEntity.ok(clientRepository.findClientsForMap());
    }

    @Override
    public HttpEntity<?> editClientData(ReqClientSave client, UUID id) throws Exception {
        Client oldClient = clientRepository.findById(id).orElseThrow(() -> new Exception("This client is not found!"));
        Client save = clientRepository.save(new Client(
                        id,
                        client.getName(),
                        client.getAddress(),
                        client.getTelephone(),
                        client.getTin(),
                        client.isActive(),
                        client.getCompanyName(),
                        customerCategoryRepository.findById(client.getCategoryId()).orElseThrow(() -> new Exception("This category was not found!")),
                        territoryRepository.findById(client.getTerritoryId()).orElseThrow(() -> new Exception("This territory was not found!")),
                        client.getLongitude(),
                        client.getLatitude(),
                        null, // this section should be changed in the next milestones
                        client.getReferencePoint(),
                        oldClient.getRegistration_date()
                )
        );
        return ResponseEntity.ok(save);
    }


}
