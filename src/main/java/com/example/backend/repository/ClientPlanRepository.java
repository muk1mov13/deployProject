package com.example.backend.repository;

import com.example.backend.entity.ClientPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientPlanRepository extends JpaRepository<ClientPlan, UUID> {
}
