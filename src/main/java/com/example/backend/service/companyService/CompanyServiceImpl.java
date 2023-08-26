package com.example.backend.service.companyService;

import com.example.backend.entity.Company;
import com.example.backend.repository.CompanyRepository;
import com.example.backend.service.fileService.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final FileService fileService;

    public CompanyServiceImpl(CompanyRepository companyRepository, FileService fileService) {
        this.companyRepository = companyRepository;
        this.fileService = fileService;
    }

    @Override
    public HttpEntity<?> getSupportTelNumber() {
        List<Company> companies = companyRepository.findAll();
        return ResponseEntity.ok(!companies.isEmpty() && !(companies.get(0).getSupport_phone() == null) ? companies.get(0).getSupport_phone() : null);
    }


    //    getcompanies
    @Override
    public HttpEntity<?> getCompany(Integer page, Integer size, String search) {
        Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size);
        Page<Company> companies = companyRepository.findAllByRegionContainsIgnoreCaseOrCompany_nameContainsIgnoreCaseOrSupport_phoneContainsIgnoreCaseOrEmailContainsIgnoreCaseOrAddressContainsIgnoreCase(search, pageable);
        return ResponseEntity.ok(companies);
    }

    @Override
    public HttpEntity<?> getExcelFile(String search, List<String> columnNames, HttpServletResponse response) {
        Pageable pageable = Pageable.unpaged();
        Page<Company> filteredCompanies = companyRepository.findAllByRegionContainsIgnoreCaseOrCompany_nameContainsIgnoreCaseOrSupport_phoneContainsIgnoreCaseOrEmailContainsIgnoreCaseOrAddressContainsIgnoreCase(search,pageable);
        System.out.println(filteredCompanies.getContent());
        return fileService.createExcelFile(filteredCompanies.getContent(),columnNames,Company.class, false);
    }




}
