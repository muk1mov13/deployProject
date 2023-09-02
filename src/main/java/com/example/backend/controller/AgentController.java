package com.example.backend.controller;

import com.example.backend.entity.Agent;
import com.example.backend.service.agentService.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentService agentService;

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping
    public HttpEntity<?> getAgents(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "") Boolean active,
            @RequestParam(defaultValue = "") String search
    ) {
        return agentService.getAgents(page, size, active, search);
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @PostMapping
    public HttpEntity<?> saveAgent(@RequestBody Agent agent) {
        return agentService.saveAgent(agent);
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @PutMapping("{id}")
    public HttpEntity<?> updateAgent(@RequestBody Agent agent, @PathVariable UUID id) {
        return agentService.updateAgent(agent, id);
    }

}
