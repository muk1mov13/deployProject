
package com.example.backend.service.customerCategoryService;
import com.example.backend.repository.CustomerCategoryRepository;
import com.example.backend.service.fileService.FileService;
import org.springframework.stereotype.Service;
import com.example.backend.entity.CustomerCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerCategoryServiceImpl implements CustomerCategoryService {
    private final CustomerCategoryRepository customerCategoryRepository;
    private final FileService fileService;
    public CustomerCategoryServiceImpl(CustomerCategoryRepository customerCategoryRepository, FileService fileService) {
        this.customerCategoryRepository = customerCategoryRepository;
        this.fileService = fileService;
    }

    @Override
    public HttpEntity<?> getAllCustomerCategories() {
        return ResponseEntity.ok(customerCategoryRepository.findAllCustomerCategory());
    }

    @Override
    public HttpEntity<?> getCustomerCategories(Integer page, Integer size, Boolean active, String search) {
        Pageable pageable = size==-1 ? Pageable.unpaged() : PageRequest.of(page > 0 ? page - 1 : 0, size);
        if (active == null) {
            Page<CustomerCategory> customerCategories = customerCategoryRepository. findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCaseOrderByCreatedAtDesc(search, search, search, pageable);
            return ResponseEntity.ok(customerCategories);
        }
        Page<CustomerCategory> customerCategories = customerCategoryRepository.findAllActiveAndByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCase(active, search, pageable);
        return ResponseEntity.ok(customerCategories);
    }

    @Override
    public HttpEntity<?> saveCustomerCategory(CustomerCategory customerCategory) {
        customerCategory.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        CustomerCategory savedCustomerCategory = customerCategoryRepository.save(customerCategory);
        return ResponseEntity.ok(savedCustomerCategory);
    }

    @Override
    public HttpEntity<?> updateCustomerCategory(UUID id, CustomerCategory customerCategory) {
        CustomerCategory findedCustomerCategory = customerCategoryRepository.findById(id).orElseThrow();
        findedCustomerCategory.setName(customerCategory.getName());
        findedCustomerCategory.setDescription(customerCategory.getDescription());
        findedCustomerCategory.setCode(customerCategory.getCode());
        findedCustomerCategory.setActive(customerCategory.isActive());
        CustomerCategory saved = customerCategoryRepository.save(findedCustomerCategory);
        return ResponseEntity.ok(saved);
    }

    @Override
    public HttpEntity<?> getExcelFile(Boolean active, String search, List<String> columnNames) {
        Pageable pageable = Pageable.unpaged();
        Page<CustomerCategory> filteredCategories = customerCategoryRepository.findAllActiveAndByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCase(active, search, pageable);
        return fileService.createExcelFile(filteredCategories.getContent(),columnNames,CustomerCategory.class, false);
    }


}


