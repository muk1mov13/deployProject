package com.example.backend.service.agentService;

import com.example.backend.entity.Agent;
import org.springframework.http.HttpEntity;

import java.util.UUID;

public interface AgentService {
    HttpEntity<?> getAgents(Integer page, Integer size, Boolean active, String search);

    HttpEntity<?> saveAgent(Agent agent);

    HttpEntity<?> updateAgent(Agent agent, UUID id);

}
