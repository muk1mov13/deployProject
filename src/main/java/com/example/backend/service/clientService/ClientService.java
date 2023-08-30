package com.example.backend.service.clientService;

import com.example.backend.payload.request.ReqClientSave;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;

import java.util.List;
import java.util.UUID;

public interface ClientService {


    HttpEntity<?> getClientsByFilter(Boolean active,
                                     String location,
                                     List<UUID> cities,
                                     List<String> days,
                                     Boolean tin,
                                     List<UUID> categories,
                                     String inventory,
                                     int pageNumber,
                                     int pageSize,
                                     String search
    );


    HttpEntity<?> getExcelFile(Boolean active,
                               String location,
                               List<UUID> cities,
                               List<String> days,
                               Boolean tin,
                               List<UUID> categories,
                               String inventory,
                               String search,
                               List<String> columnNames,
                               HttpServletResponse response);

    HttpEntity<?> saveNewClient(ReqClientSave newClient);

    HttpEntity<?> getClientsForMap(String cities);

    HttpEntity<?> editClientData(ReqClientSave newClient,UUID id) throws Exception;
}
