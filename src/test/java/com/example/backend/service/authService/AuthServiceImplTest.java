package com.example.backend.service.authService;
import com.example.backend.entity.User;
import com.example.backend.payload.request.ReqLogin;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthServiceImpl(userRepository, authenticationManager, jwtService);
    }

    @Test
    void testLoginUser_Success() throws Exception {
        ReqLogin reqLogin = new ReqLogin("testPhoneNumber", "testPassword", true);

        when(authenticationManager.authenticate(any()))
                .then(invocation -> {
                    // Simulate successful authentication
                    return null;
                });

        User user = new User();
        user.setPhone("testPhoneNumber");
        when(userRepository.findByPhone(anyString())).thenReturn(Optional.of(user));

        when(jwtService.generateJWT(any(User.class))).thenReturn("testAccessToken");
        when(jwtService.generateJWTRefresh_Token(any(User.class))).thenReturn("testRefreshToken");
        HttpServletResponse response = new Response();
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) authService.loginUser(reqLogin, response);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("testAccessToken", responseBody.get("access_token"));
        assertEquals("testRefreshToken", responseBody.get("refresh_token"));

//        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void testLoginUser_InvalidCredentials() throws Exception {
        ReqLogin reqLogin = new ReqLogin("testPhoneNumber", "invalidPassword", false);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new UsernameNotFoundException("User not found!"));
        HttpServletResponse response = new Response();
        HttpEntity<?> responseEntity = authService.loginUser(reqLogin, response);

        assertNull(responseEntity);
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType("application/json");
        verify(response.getWriter()).write("Wrong phone number or password!");
    }

    @Test
    void testRefreshToken_Success() throws IOException {
        String refreshToken = "testRefreshToken";

        when(jwtService.extractSubjectFromJWT(refreshToken)).thenReturn("testPhoneNumber");

        User user = new User();
        when(userRepository.findByPhone("testPhoneNumber")).thenReturn(Optional.of(user));

        when(jwtService.generateJWT(user)).thenReturn("testAccessToken");
        HttpServletResponse response = new Response();
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) authService.refreshToken(refreshToken, response);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("testAccessToken", responseEntity.getBody());

//        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void testRefreshToken_ExpiredToken() throws IOException {
        String refreshToken = "testExpiredToken";

        when(jwtService.extractSubjectFromJWT(refreshToken))
                .thenThrow(new ExpiredJwtException(null, null, "Expired token"));
        HttpServletResponse response = new Response();
        HttpEntity<?> responseEntity = authService.refreshToken(refreshToken, response);

        assertNull(responseEntity);
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType("application/json");
        verify(response.getWriter()).write("Refresh token has expired");
    }
}
