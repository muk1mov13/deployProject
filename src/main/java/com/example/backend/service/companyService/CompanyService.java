package com.example.backend.service.companyService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;

import java.util.List;

public interface CompanyService {
    HttpEntity<?> getSupportTelNumber();

//    getcompanies


    //    getcompanies
    HttpEntity<?> getCompany(Integer page, Integer size, String search);

    HttpEntity<?> getExcelFile(String search, List<String> columnNames, HttpServletResponse response);
}
