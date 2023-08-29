package com.example.backend.repository;

import com.example.backend.entity.CustomerCategory;
import com.example.backend.projection.CustomerCategoryProjection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerCategoryRepositoryTest {

    @Autowired
    private CustomerCategoryRepository customerCategoryRepository;

    @BeforeEach
    public void setUp() {
        CustomerCategory testedCategory = CustomerCategory
                .builder()
                .id(UUID.randomUUID())
                .name("test")
                .description("tested category")
                .code("123")
                .active(false)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        customerCategoryRepository.save(testedCategory);

    }

    @Test
    public void testFindAllCustomerCategoriesForBot() {
        // Create a sample pageable object
        Pageable pageable = PageRequest.of(0, 10);

        // Call the findAllCustomerCategoriesForBot method
        Page<CustomerCategory> customerCategories = customerCategoryRepository.findAllCustomerCategoriesForBot(pageable);

        // Assert that the returned page contains customer categories
        Assertions.assertFalse(customerCategories.isEmpty());
    }

    @Test
    public void testFindByName() {
        // Create a sample customer category
        CustomerCategory customerCategory = CustomerCategory
                .builder()
                .id(UUID.randomUUID())
                .name("Test Category")
                .description("category")
                .code("1234")
                .active(false)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        // Save the customer category to the repository
        customerCategoryRepository.save(customerCategory);

        // Call the findByName method
        Optional<CustomerCategory> foundCategoryOptional = customerCategoryRepository.findByName("Test Category");

        // Assert that the customer category was found
        Assertions.assertTrue(foundCategoryOptional.isPresent());
        CustomerCategory foundCategory = foundCategoryOptional.get();
        Assertions.assertEquals("Test Category", foundCategory.getName());
    }

    @Test
    public void testFindAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCaseOrderByCreatedAtDesc() {
        // Create sample customer categories
        CustomerCategory category1 = new CustomerCategory();
        category1.setName("Category 1");
        category1.setDescription("Description 1");
        category1.setCode("Code 1");
        category1.setActive(false);
        category1.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        CustomerCategory category2 = new CustomerCategory();
        category2.setName("Category 2");
        category2.setDescription("Description 2");
        category2.setCode("Code 2");
        category2.setActive(true);
        category2.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        // Save the customer categories to the repository
        customerCategoryRepository.save(category1);
        customerCategoryRepository.save(category2);

        // Create a sample pageable object
        Pageable pageable = PageRequest.of(0, 10);

        // Call the findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCaseOrderByCreatedAtDesc method
        Page<CustomerCategory> customerCategories = customerCategoryRepository.findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCaseOrderByCreatedAtDesc(
                "Category", "Description", "Code", pageable);

        // Assert that the page contains the expected customer categories
        Assertions.assertEquals(2, customerCategories.getTotalElements());
        Assertions.assertEquals("Category 1", customerCategories.getContent().get(0).getName());
        Assertions.assertEquals("Category 2", customerCategories.getContent().get(1).getName());
    }

    @Test
    public void testFindAllCustomerCategory() {
        // Call the findAllCustomerCategory method
        List<CustomerCategoryProjection> customerCategories = customerCategoryRepository.findAllCustomerCategory();

        // Assert that the returned list is not empty
        Assertions.assertFalse(customerCategories.isEmpty());
    }

    @Test
    public void testFindAllActiveAndByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCase() {
        // Create sample customer categories
        CustomerCategory category1 = new CustomerCategory();
        category1.setName("Category 1");
        category1.setDescription("Description 1");
        category1.setCode("Code 1");
        category1.setActive(true);
        category1.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        CustomerCategory category2 = new CustomerCategory();
        category2.setName("Category 2");
        category2.setDescription("Description 2");
        category2.setCode("Code 2");
        category2.setActive(false);
        category2.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        // Save the customer categories to the repository
        customerCategoryRepository.save(category1);
        customerCategoryRepository.save(category2);

        // Create a sample pageable object
        Pageable pageable = PageRequest.of(0, 10);

        // Call the findAllActiveAndByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCase method
        Page<CustomerCategory> activeCategories = customerCategoryRepository.findAllActiveAndByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCase(
                true, "Category", pageable);

        // Assert that the page contains the expected active customer categories
        Assertions.assertEquals(1, activeCategories.getTotalElements());
        Assertions.assertEquals("Category 1", activeCategories.getContent().get(0).getName());
    }
}