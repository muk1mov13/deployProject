package com.example.backend.service.authService;
import com.example.backend.payload.request.ReqLogin;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

    public class AuthServiceImplTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private AuthenticationManager authenticationManager;

        @Mock
        private JwtService jwtService;

        @InjectMocks
        private AuthServiceImpl authService;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void testLoginUser() throws IOException {
            ReqLogin reqLogin = new ReqLogin("testphone", "testpassword", true);
            User user = new User();
            user.setPhone("testphone");
            when(userRepository.findByPhone("testphone")).thenReturn(Optional.of(user));
            when(jwtService.generateJWT(user)).thenReturn("test_access_token");
            when(jwtService.generateJWTRefresh_Token(user)).thenReturn("test_refresh_token");
            HttpServletResponse res = new Response();
            ResponseEntity<?> response = (ResponseEntity<?>) authService.loginUser(reqLogin,res);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody() instanceof Map);
            Map<String, String> responseBody = (Map<String, String>) response.getBody();
            assertTrue(responseBody.containsKey("access_token"));
            assertEquals("test_access_token", responseBody.get("access_token"));
            assertTrue(responseBody.containsKey("refresh_token"));
            assertEquals("test_refresh_token", responseBody.get("refresh_token"));
            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(userRepository, times(1)).findByPhone("testphone");
            verify(jwtService, times(1)).generateJWT(user);
            verify(jwtService, times(1)).generateJWTRefresh_Token(user);
        }

        @Test
        public void testLoginUser_UserNotFound() {
            ReqLogin reqLogin = new ReqLogin("testphone", "testpassword", true);
            when(userRepository.findByPhone("testphone")).thenReturn(Optional.empty());
            HttpServletResponse res = new Response();
            assertThrows(UsernameNotFoundException.class, () -> authService.loginUser(reqLogin,res));
            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(userRepository, times(1)).findByPhone("testphone");
            verify(jwtService, never()).generateJWT(any(User.class));
            verify(jwtService, never()).generateJWTRefresh_Token(any(User.class));
        }


}
