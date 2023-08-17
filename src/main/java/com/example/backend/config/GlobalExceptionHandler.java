package com.example.backend.config;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.io.IOException;
import java.util.NoSuchElementException;

@ControllerAdvice(basePackages = {"com.example.backend.security"})
public class GlobalExceptionHandler {

//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<String> handleNoSuchElementException() {
//        return new ResponseEntity<>("No one found with provided phone", HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    public void handleExpiredJwtException(HttpServletResponse response) throws IOException {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType("application/json");
//        response.getWriter().write("xsx");
//        response.getWriter().close();
//    }
//
}
