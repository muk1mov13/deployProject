package com.example.backend.service.authService;

import com.example.backend.payload.request.ReqLogin;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;


import java.io.IOException;


public interface AuthService {

    HttpEntity<?> loginUser(ReqLogin reqLogin, HttpServletResponse response) throws IOException;

    HttpEntity<?> refreshToken(String refreshToken, HttpServletResponse response) throws IOException;
}
