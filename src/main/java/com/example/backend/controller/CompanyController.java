package com.example.backend.controller;

import com.example.backend.service.companyService.CompanyService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/support_phone/public")
    public HttpEntity<?> getSupportTelNumber() {
        return companyService.getSupportTelNumber();
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping
    public HttpEntity<?> getCompanies(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "") String search
    ) {
        return companyService.getCompany(page, size, search);
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/excel")
    public HttpEntity<?> getExcelFile(@RequestParam(defaultValue = "") String search,
                                      @RequestParam(defaultValue = "id") List<String> columnNames,
                                      HttpServletResponse response) {
        return companyService.getExcelFile(
                search,
                columnNames,
                response);
    }


}
