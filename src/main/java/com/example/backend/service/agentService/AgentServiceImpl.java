package com.example.backend.service.agentService;

import com.example.backend.entity.Agent;
import com.example.backend.repository.AgentRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;

    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Override
    public HttpEntity<?> getAgents() {
        List<Agent> agents = agentRepository.findAll();
        return ResponseEntity.ok(agents);
    }

    @Override
    public HttpEntity<?> saveAgent(Agent agent) {
        Agent saved = agentRepository.save(agent);
        return ResponseEntity.ok(saved);
    }

    @Override
    public HttpEntity<?> updateAgent(Agent agent, UUID id) {
        Agent hasAgent = agentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("not found"));
        hasAgent.setName(agent.getName());
        hasAgent.setPhone(agent.getPhone());
        hasAgent.setChatId(agent.getChatId());
        Agent updateSavedAgent = agentRepository.save(hasAgent);
        return ResponseEntity.ok(updateSavedAgent);
    }
}
