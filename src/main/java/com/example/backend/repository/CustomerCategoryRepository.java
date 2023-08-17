package com.example.backend.repository;
import com.example.backend.entity.CustomerCategory;
import com.example.backend.projection.CustomerCategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface CustomerCategoryRepository extends JpaRepository<CustomerCategory, UUID> {
    Page<CustomerCategory> findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCaseOrderById(String name, String description, String code, Pageable pageable);

    @Query(value = "SELECT * FROM customer_category ", nativeQuery = true)
    Page<CustomerCategory> findAllCustomerCategoriesForBot(Pageable pageable);

    //    for tg bot
    @Query(value = "SELECT *  FROM customer_category WHERE name=:name", nativeQuery = true)
    Optional<CustomerCategory> findByName(String name);

    Page<CustomerCategory> findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCaseOrderByCreatedAtDesc(String name, String description, String code, Pageable pageable);
    @Query(value = """
          select c.id as value, c.name as label from CustomerCategory c order by c.name
          """)
    List<CustomerCategoryProjection> findAllCustomerCategory();

    @Query(
            value = "SELECT * FROM customer_category " +
                    "WHERE (:active is null or active = :active) " +
                    "AND (lower(name) LIKE lower(concat('%', :search, '%')) " +
                    "OR lower(description) LIKE lower(concat('%', :search, '%')) " +
                    "OR lower(code) LIKE lower(concat('%', :search, '%'))) ORDER BY created_at DESC ",
            nativeQuery = true)
    Page<CustomerCategory> findAllActiveAndByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCodeContainsIgnoreCase(
            @Param("active") Boolean active,
            @Param("search") String search,
            Pageable pageable
    );


}

