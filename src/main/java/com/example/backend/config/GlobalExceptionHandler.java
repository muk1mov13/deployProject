package com.example.backend.config;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({SignatureException.class, MalformedJwtException.class,
            ExpiredJwtException.class, UnsupportedJwtException.class,
            IllegalArgumentException.class})
    public ResponseEntity<?> handleJwtException(Exception ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Map<String, String> body = new HashMap<>();

        if (ex instanceof SignatureException) {
            body.put("error", "Invalid signature");
            System.out.println("lalallalalallaa");
        } else if (ex instanceof MalformedJwtException) {
            body.put("error", "Invalid token");
            System.out.println("lalallalalallaa");
        } else if (ex instanceof ExpiredJwtException) {
            body.put("error", "Expired token");
            System.out.println("lalallalalallaa");
        } else if (ex instanceof UnsupportedJwtException) {
            body.put("error", "Unsupported token");
            System.out.println("lalallalalallaa");
        } else if (ex instanceof IllegalArgumentException) {
            body.put("error", "Token is empty");
            System.out.println("lalallalalallaa");
        }

        return new ResponseEntity<>(body, status);
    }
}