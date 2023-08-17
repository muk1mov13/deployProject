package com.example.backend.service.userService;

import com.example.backend.entity.User;
import com.example.backend.projection.DashboardViewProjection;
import com.example.backend.repository.CompanyRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class UserServiceImpl implements UserService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public UserServiceImpl(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public HttpEntity<?> getDashboardViewPart(User user) {
        if (user == null) {
            return ResponseEntity.status(403).body("user is null");
        }
        DashboardViewProjection userDashboardViewProjection = companyRepository.getDashboardInfoBySuperVisorId(user.getId()).orElseThrow(() -> new NoSuchElementException("not found"));
        return ResponseEntity.ok(userDashboardViewProjection);
    }
}
