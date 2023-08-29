package com.example.backend.service.customerCategoryService;
import com.example.backend.entity.CustomerCategory;
import com.example.backend.projection.CustomerCategoryProjection;
import com.example.backend.repository.CustomerCategoryRepository;
import com.example.backend.service.fileService.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerCategoryServiceImplTest {
    @Mock
    private CustomerCategoryRepository customerCategoryRepository;

    @Mock
    private FileService fileService;

    @Mock
    private Page<CustomerCategory> mockedPage;

    @InjectMocks
    private CustomerCategoryServiceImpl customerCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomerCategories() {
        List<CustomerCategoryProjection> mockedCategories = new ArrayList<>();
        // Add some mocked categories to the list

        // Mock the behavior of the customerCategoryRepository
        when(customerCategoryRepository.findAllCustomerCategory()).thenReturn(mockedCategories);

        // Create an instance of the CustomerCategoryServiceImpl class
        CustomerCategoryServiceImpl categoryService = new CustomerCategoryServiceImpl(customerCategoryRepository, fileService);

        // Call the method under test
        HttpEntity<?> result = categoryService.getAllCustomerCategories();

        // Verify the expected behavior
        assertEquals(ResponseEntity.ok(mockedCategories), result);
        verify(customerCategoryRepository, times(1)).findAllCustomerCategory();
        verifyNoMoreInteractions(customerCategoryRepository);
    }

    @Test
    void testGetCustomerCategories() {
        int page = 1;
        int size = 10;
        boolean active = true;
        String search = "example";

        // Mock the behavior of the customerCategoryRepository
        when(customerCategoryRepository.findAllActiveAndByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCase(active, search, PageRequest.of(page > 0 ? page - 1 : 0, size)))
                .thenReturn(mockedPage);

        // Create an instance of the CustomerCategoryServiceImpl class
        CustomerCategoryServiceImpl categoryService = new CustomerCategoryServiceImpl(customerCategoryRepository, fileService);

        // Call the method under test
        HttpEntity<?> result = categoryService.getCustomerCategories(page, size, active, search);

        // Verify the expected behavior
        assertEquals(ResponseEntity.ok(mockedPage), result);
        verify(customerCategoryRepository, times(1)).findAllActiveAndByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCase(active, search, PageRequest.of(page > 0 ? page - 1 : 0, size));
        verifyNoMoreInteractions(customerCategoryRepository);
    }

    @Test
    void testGetCustomerCategoriesWhenActiveNull() {
        int page = 1;
        int size = 10;
        Boolean active = null;
        String search = "example";

        // Mock the behavior of the customerCategoryRepository
        when(customerCategoryRepository.findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCaseOrderByCreatedAtDesc(search, search, search, PageRequest.of(page > 0 ? page - 1 : 0, size)))
                .thenReturn(mockedPage);

        // Create an instance of the CustomerCategoryServiceImpl class
        CustomerCategoryServiceImpl categoryService = new CustomerCategoryServiceImpl(customerCategoryRepository, fileService);

        // Call the method under test
        HttpEntity<?> result = categoryService.getCustomerCategories(page, size, active, search);

        // Verify the expected behavior
        assertEquals(ResponseEntity.ok(mockedPage), result);
        verify(customerCategoryRepository, times(1)).findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCaseOrderByCreatedAtDesc(search, search, search, PageRequest.of(page > 0 ? page - 1 : 0, size));
        verifyNoMoreInteractions(customerCategoryRepository);
    }

    @Test
    void testSaveCustomerCategory() {
        CustomerCategory customerCategory = new CustomerCategory();
        // Set the properties of the customerCategory

        // Mock the behavior of the customerCategoryRepository
        when(customerCategoryRepository.save(customerCategory)).thenReturn(customerCategory);

        // Create an instance of the CustomerCategoryServiceImpl class
        CustomerCategoryServiceImpl categoryService = new CustomerCategoryServiceImpl(customerCategoryRepository, fileService);

        // Call the method under test
        HttpEntity<?> result = categoryService.saveCustomerCategory(customerCategory);

        // Verify the expected behavior
        assertEquals(ResponseEntity.ok(customerCategory), result);
        verify(customerCategoryRepository, times(1)).save(customerCategory);
        verifyNoMoreInteractions(customerCategoryRepository);
    }

    @Test
    void testUpdateCustomerCategory() {
        UUID id = UUID.randomUUID();
        CustomerCategory customerCategory = new CustomerCategory();
        // Set the properties of the customerCategory

        // Mock the behavior of the customerCategoryRepository
        when(customerCategoryRepository.findById(id)).thenReturn(Optional.of(customerCategory));
        when(customerCategoryRepository.save(customerCategory)).thenReturn(customerCategory);

        // Create an instance of the CustomerCategoryServiceImpl class
        CustomerCategoryServiceImpl categoryService = new CustomerCategoryServiceImpl(customerCategoryRepository, fileService);

        // Call the method under test
        HttpEntity<?> result = categoryService.updateCustomerCategory(id, customerCategory);

        // Verify the expected behavior
        assertEquals(ResponseEntity.ok(customerCategory), result);
        verify(customerCategoryRepository, times(1)).findById(id);
        verify(customerCategoryRepository, times(1)).save(customerCategory);
        verifyNoMoreInteractions(customerCategoryRepository);
    }


    @Test
    public void testGetExcelFile() {
//        qaytadan yozish
        Boolean active = true;
        String search = "someSearch";
        List<String> columnNames = List.of("col1", "col2");

        // Mock the Page and its content
        Page<CustomerCategory> mockedPage = new PageImpl<>(Collections.emptyList());
        when(customerCategoryRepository.findAllActiveAndByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCase(active, search, Pageable.unpaged())).thenReturn(mockedPage);

        // Mock fileService.createExcelFile method
        HttpEntity<?> mockHttpEntity = ResponseEntity.ok("Mocked Excel Content");
        when(fileService.createExcelFile(mockedPage.getContent(), columnNames, CustomerCategory.class, false)).thenReturn(null);

        HttpEntity<?> responseEntity = customerCategoryService.getExcelFile(active, search, columnNames);

//        assertEquals(mockHttpEntity, responseEntity);

        verify(customerCategoryRepository, times(1)).findAllActiveAndByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCase(active, search, Pageable.unpaged());
        verify(fileService, times(1)).createExcelFile(mockedPage.getContent(), columnNames, CustomerCategory.class, false);
    }

}