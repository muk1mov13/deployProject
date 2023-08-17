package com.example.backend.controller;

import com.example.backend.payload.request.ReqLogin;
import com.example.backend.service.authService.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/public")
    public HttpEntity<?> login(@RequestBody ReqLogin reqLogin, HttpServletResponse response) throws IOException {
        return authService.loginUser(reqLogin,response);
    }

    @PostMapping("/refresh/public")
    public HttpEntity<?> refreshToken(@RequestParam String refresh_token, HttpServletResponse response) throws IOException {
        return authService.refreshToken(refresh_token,response);
    }

}
