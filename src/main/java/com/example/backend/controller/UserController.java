package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.security.CurrentUser;
import com.example.backend.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    @GetMapping("/me/public")
    public HttpEntity<?> getCurrentUser(@CurrentUser User currentUser) {
        return ResponseEntity.ok(currentUser);
    }

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @GetMapping("/dashboard")
    public HttpEntity<?> getDashboardViewPart(@CurrentUser User user) {
        return userService.getDashboardViewPart(user);
    }

}
