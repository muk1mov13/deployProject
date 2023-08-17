package com.example.backend.repository;

import com.example.backend.entity.Company;
import com.example.backend.entity.User;
import com.example.backend.projection.DashboardViewProjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetDashboardInfoBySuperVisorId_Success() {
        // Create a supervisor user and save it in the database
        User supervisor = new User();
        supervisor.setName("John Doe");
        supervisor.setPhone("123456789");
        supervisor.setPassword("password");
        supervisor.setRoles(null);
        supervisor = userRepository.save(supervisor);

        // Create and save a Company entity with the supervisor user
        Company company = new Company();
        company.setRegion("Region");
        company.setSupervisor(supervisor);
        company.setCompany_name("Company Name");
        company.setSupport_phone("123-456-7890");
        company.setEmail("company@example.com");
        company.setAddress("Company Address");
        companyRepository.save(company);

        // Get the projected data using the repository method that returns the projection
        Optional<DashboardViewProjection> dashboardProjection = companyRepository.getDashboardInfoBySuperVisorId(supervisor.getId());

        // Assert that the projection is not empty and has the expected values
        assertTrue(dashboardProjection.isPresent());
        DashboardViewProjection projection = dashboardProjection.get();
        assertEquals("123-456-7890", projection.getSupportPhone());
        assertEquals(LocalDate.now(), projection.getCurrentDate());
    }

    @Test
    public void testGetDashboardInfoBySuperVisorId_NotFound() {
        UUID nonExistentSupervisorId = UUID.randomUUID();
        Optional<DashboardViewProjection> dashboardProjection = companyRepository.getDashboardInfoBySuperVisorId(nonExistentSupervisorId);
        assertTrue(dashboardProjection.isEmpty());
    }

}
