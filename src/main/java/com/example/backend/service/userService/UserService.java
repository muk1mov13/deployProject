package com.example.backend.service.userService;

import com.example.backend.entity.User;
import org.springframework.http.HttpEntity;



public interface UserService {

    HttpEntity<?> getDashboardViewPart(User user);

}