package com.example.backend.repository;

import com.example.backend.entity.Agent;
import com.example.backend.entity.Territory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AgentRepository extends JpaRepository<Agent, UUID> {
    Optional<Agent> findByPhone(String phone);

    @Query("SELECT a FROM Agent a WHERE LOWER(a.name) LIKE %:search% OR LOWER(a.phone) LIKE %:search% ORDER BY a.created_at DESC")
    Page<Agent> findAllByNameContainsIgnoreCaseOrPhoneContainsIgnoreCaseOrderByCreated_atDesc(@Param("search") String searchTerm,Pageable pageable);

}
