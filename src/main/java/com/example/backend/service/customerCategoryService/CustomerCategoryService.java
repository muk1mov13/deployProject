
package com.example.backend.service.customerCategoryService;

import com.example.backend.entity.CustomerCategory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;

import java.util.List;
import java.util.UUID;

public interface CustomerCategoryService {
    HttpEntity<?> getAllCustomerCategories();

    HttpEntity<?> getCustomerCategories(Integer page, Integer size, Boolean active, String search);

    HttpEntity<?> saveCustomerCategory(CustomerCategory customerCategory);

    HttpEntity<?> updateCustomerCategory(UUID id, CustomerCategory customerCategory);
    HttpEntity<?> getExcelFile(Boolean active, String search, List<String> columnNames);
}

