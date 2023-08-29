package com.example.backend.service.companyService;

import com.example.backend.entity.Company;
import com.example.backend.repository.CompanyRepository;
import com.example.backend.service.fileService.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Mock
    private FileService fileService;

    @Mock
    private Page<Company> mockedPage;

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

    @Test
    void testGetCompany() {
        int page = 1;
        int size = 10;
        String search = "example";

        // Mock the behavior of the companyRepository
        when(companyRepository.findAllByRegionContainsIgnoreCaseOrCompany_nameContainsIgnoreCaseOrSupport_phoneContainsIgnoreCaseOrEmailContainsIgnoreCaseOrAddressContainsIgnoreCase(search, PageRequest.of(page > 0 ? page - 1 : 0, size)))
                .thenReturn(mockedPage);

        // Create an instance of the CompanyServiceImpl class
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository, fileService);

        // Call the method under test
        HttpEntity<?> result = companyService.getCompany(page, size, search);

        // Verify the expected behavior
        assertEquals(ResponseEntity.ok(mockedPage), result);
        verify(companyRepository, times(1)).findAllByRegionContainsIgnoreCaseOrCompany_nameContainsIgnoreCaseOrSupport_phoneContainsIgnoreCaseOrEmailContainsIgnoreCaseOrAddressContainsIgnoreCase(search, PageRequest.of(page > 0 ? page - 1 : 0, size));
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    void testGetExcelFile() {
        String search = "example";
        List<String> columnNames = new ArrayList<>();
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock the behavior of the companyRepository
        when(companyRepository.findAllByRegionContainsIgnoreCaseOrCompany_nameContainsIgnoreCaseOrSupport_phoneContainsIgnoreCaseOrEmailContainsIgnoreCaseOrAddressContainsIgnoreCase(search, Pageable.unpaged()))
                .thenReturn(mockedPage);

        // Create an instance of the CompanyServiceImpl class
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository, fileService);

        // Call the method under test
        HttpEntity<?> result = companyService.getExcelFile(search, columnNames, response);

        // Verify the expected behavior
        assertEquals(result, null); // Modify this assertion based on the expected behavior of fileService.createExcelFile()
        verify(companyRepository, times(1)).findAllByRegionContainsIgnoreCaseOrCompany_nameContainsIgnoreCaseOrSupport_phoneContainsIgnoreCaseOrEmailContainsIgnoreCaseOrAddressContainsIgnoreCase(search, Pageable.unpaged());
        verify(fileService, times(1)).createExcelFile(mockedPage.getContent(), columnNames, Company.class, false);
        verifyNoMoreInteractions(companyRepository, fileService);
    }


}
