package com.example.backend.controller;

import com.example.backend.entity.Agent;
import com.example.backend.service.agentService.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentService agentService;

    @PreAuthorize("hasRole('ROLE_AGENT')")
    @GetMapping
    public HttpEntity<?> getAgents() {
        return agentService.getAgents();
    }

    @PreAuthorize("hasRole('ROLE_AGENT')")
    @PostMapping
    public HttpEntity<?> saveAgent(@RequestBody Agent agent) {
        return agentService.saveAgent(agent);
    }

    @PreAuthorize("hasRole('ROLE_AGENT')")
    @PutMapping("{id}")
    public HttpEntity<?> updateAgent(@RequestBody Agent agent, @PathVariable UUID id) {
        return agentService.updateAgent(agent, id);
    }

}
