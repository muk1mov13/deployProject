package com.example.backend.service.companyService;
import com.example.backend.entity.Company;
import com.example.backend.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    public class CompanyServiceImplTest {
        @Mock
        private CompanyRepository companyRepository;

        @InjectMocks
        private CompanyServiceImpl companyService;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void testGetSupportTelNumber() {
            List<Company> companies = new ArrayList<>();
            Company company = new Company();
            company.setSupport_phone("123456789");
            companies.add(company);
            when(companyRepository.findAll()).thenReturn(companies);
            HttpEntity<?> response = companyService.getSupportTelNumber();
            assertEquals(ResponseEntity.ok("123456789"), response);
            verify(companyRepository, times(1)).findAll();
        }

        @Test
        public void testGetSupportTelNumber_NoCompanies() {
            List<Company> companies = new ArrayList<>();
            when(companyRepository.findAll()).thenReturn(companies);
            HttpEntity<?> response = companyService.getSupportTelNumber();
            assertEquals(ResponseEntity.ok(null), response);
            verify(companyRepository, times(1)).findAll();
        }


}
