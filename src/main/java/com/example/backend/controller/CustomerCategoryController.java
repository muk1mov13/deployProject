
package com.example.backend.controller;

import com.example.backend.entity.CustomerCategory;
import com.example.backend.service.customerCategoryService.CustomerCategoryService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer_category")
@RequiredArgsConstructor
public class CustomerCategoryController {
    private final CustomerCategoryService customerCategoryService;
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/all")
    public HttpEntity<?> getAllCustomerCategories(){
        return customerCategoryService.getAllCustomerCategories();
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping
    public HttpEntity<?> getCustomerCategories(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "") Boolean active,
            @RequestParam(defaultValue = "") String search
    ) {
        return customerCategoryService.getCustomerCategories(page, size, active, search);
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @PostMapping
    public HttpEntity<?> saveCustomerCategory(@RequestBody CustomerCategory customerCategory) {
        return customerCategoryService.saveCustomerCategory(customerCategory);
    }
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @PutMapping("{id}")
    public HttpEntity<?> updateCustomerCategory(@PathVariable UUID id, @RequestBody CustomerCategory customerCategory) {
        return customerCategoryService.updateCustomerCategory(id, customerCategory);
    }
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/excel")
    public HttpEntity<?> getExcelFile(@RequestParam(defaultValue = "") Boolean active,
                                      @RequestParam(defaultValue = "") String search,
                                      @RequestParam(defaultValue = "id") List<String> columnNames,
                                      HttpServletResponse response) {
        return customerCategoryService.getExcelFile(active,
                search,
                columnNames);
    }

}

