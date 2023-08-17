package com.example.backend.service.userService;

import com.example.backend.entity.User;
import com.example.backend.projection.DashboardViewProjection;
import com.example.backend.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    public void testGetDashboardViewPart_Success() {
        // Create a sample User
        User user = new User();
        user.setId(UUID.randomUUID());

        // Create a mock DashboardViewProjection
        DashboardViewProjection dashboardProjection = mock(DashboardViewProjection.class);
        when(dashboardProjection.getSupportPhone()).thenReturn("123456789"); // Replace with the expected support phone value
        when(dashboardProjection.getCurrentDate()).thenReturn(LocalDate.now());

        // Mock the companyRepository to return the mock DashboardViewProjection
        when(companyRepository.getDashboardInfoBySuperVisorId(user.getId())).thenReturn(Optional.of(dashboardProjection));

        // Call the method under test
        HttpEntity<?> response = userService.getDashboardViewPart(user);

        // Verify that the companyRepository.getDashboardInfoBySuperVisorId was called with the correct user ID
        verify(companyRepository).getDashboardInfoBySuperVisorId(user.getId());

        // Assert the response
        assertNotNull(response.getBody());

        // If you expect the response body to be a DashboardViewProjection object, you can assert its properties
        DashboardViewProjection responseBody = (DashboardViewProjection) response.getBody();
        assertEquals("123456789", responseBody.getSupportPhone());
        assertEquals(LocalDate.now(), responseBody.getCurrentDate());
    }

    @Test
    public void testGetDashboardViewPart_UserNull() {
        // Call the method under test with a null user
        ResponseEntity<?> response = (ResponseEntity<?>) userService.getDashboardViewPart(null);

        // Assert the response
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("user is null", response.getBody());
    }
}
