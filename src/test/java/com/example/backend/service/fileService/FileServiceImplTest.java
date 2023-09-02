package com.example.backend.service.fileService;

import com.example.backend.entity.CustomerCategory;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FileServiceImplTest {

    private FileServiceImpl fileService = new FileServiceImpl();

    @Test
    void testCreateExcelFile() {
        // Set up test data
        List<CustomerCategory> filteredObjects = new ArrayList<>(); // Replace CustomerCategory with your object type
        List<String> columnNames = new ArrayList<>(); // Add your column names
        Class<?> clazz = CustomerCategory.class; // Replace CustomerCategory with your object class
        boolean x = true; // Set the value of x according to your test case

        // Call the method under test
        HttpEntity<? extends Serializable> result = fileService.createExcelFile(filteredObjects, columnNames, clazz, x);

        // Verify the result
        byte[] expectedBytes = {};// Add your expected byte array for the Excel file
        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        expectedHeaders.setContentDispositionFormData("attachment", "customer_categories.xlsx");
        ResponseEntity<byte[]> expectedResponseEntity = new ResponseEntity<>(expectedBytes, expectedHeaders, HttpStatus.OK);
        assertEquals(expectedResponseEntity, result);

        // Verify any interactions with dependencies (if applicable)
        // Add verification statements for any relevant dependencies

    }
}