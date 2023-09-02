package com.example.backend.service.agentService;

import com.example.backend.entity.Agent;
import com.example.backend.entity.Role;
import com.example.backend.repository.AgentRepository;
import com.example.backend.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;

    private final RoleRepository roleRepository;

    public AgentServiceImpl(AgentRepository agentRepository, RoleRepository roleRepository) {
        this.agentRepository = agentRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public HttpEntity<?> getAgents(Integer page, Integer size, Boolean active, String search) {
        Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size);
        Page<Agent> searchedAgents = agentRepository.findAllByNameContainsIgnoreCaseOrPhoneContainsIgnoreCaseOrderByCreated_atDesc(search, pageable);
        return ResponseEntity.ok(searchedAgents);
    }

    @Override
    public HttpEntity<?> saveAgent(Agent agent) {
        List<Role> roleAgent = roleRepository.findByName("ROLE_AGENT");
        agent.setRoles(roleAgent);
        agent.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
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
