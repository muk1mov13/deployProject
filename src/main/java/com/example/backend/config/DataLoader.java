package com.example.backend.config;

import com.example.backend.entity.*;
import com.example.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final SettingsRepository settingsRepository;

    @Override
    public void run(String... args) {
        List<Role> roleList = roleRepository.findAll();
        if (roleList.isEmpty()) {
            List<Role> roles = createRoles();
            User savedSupervisor = createSupervisor(roles);
            createCompany(savedSupervisor);
            createSettings();
        }
    }

    public List<Role> createRoles() {
        List<Role> tempRoles = new ArrayList<>();
        tempRoles.add(new Role(1, "ROLE_SUPERVISOR"));
        tempRoles.add(new Role(2, "ROLE_AGENT"));
        return roleRepository.saveAll(tempRoles);
    }

    public User createSupervisor(List<Role> roles) {
        if (roles.isEmpty()) {
            Role roleSupervisor = roleRepository.save(new Role(1, "ROLE_SUPERVISOR"));
            roles.add(roleSupervisor);
        }
        User supervisor = new User(
                UUID.randomUUID(),
                "supervisor",
                "+998907397109",
                passwordEncoder.encode("root123"),
                roles
        );
        return userRepository.save(supervisor);
    }

    public void createCompany(User savedSupervisor) {
        if (savedSupervisor == null) {
            List<Role> roles = roleRepository.findAll();
            User supervisor = new User(
                    UUID.randomUUID(),
                    "supervisor",
                    "+998907397109",
                    passwordEncoder.encode("root123"),
                    roles);
            savedSupervisor = userRepository.save(supervisor);
        }
        Company company = new Company(
                1,
                "Tashkent",
                savedSupervisor,
                "Shift academy",
                "+998000000000",
                "company@gmail.com",
                "Tashkent city Main district",
                Timestamp.valueOf(LocalDateTime.now())
        );
        companyRepository.save(company);
    }

    public void createSettings() {
        List<String> names = List.of("Company Profile", "Territory", "Customer Category");
        for (String name : names) {
            settingsRepository.save(new Settings(UUID.randomUUID(), name));
        }
    }
}


