package com.example.backend.security;

import com.example.backend.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

//yangi security
@Component
public class JwtService {
    public String generateJWT(User user) {
        Claims claims = Jwts.claims();
        claims.put("roles", user.getRoles());
        return Jwts.builder()
                .signWith(generateSecretKey())
                .setClaims(claims)
                .setSubject(user.getPhone())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // bu 1kunlik
                .compact();
    }

    public String generateJWTRefresh_Token(User user) {
        return Jwts.builder()
                .signWith(generateSecretKey())
                .setSubject(user.getPhone())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) //bir haftalik
                .compact();
    }


    private Key generateSecretKey() {
        byte[] bytes = "O'zbekistonvatanimmanimgullayashnahuro'zbekistonshiftacademyengzroquvmarkaz".getBytes();
        return Keys.hmacShaKeyFor(bytes);
    }


    public String extractSubjectFromJWT(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(generateSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String authToken, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        try {
            Jwts.parser().setSigningKey(generateSecretKey()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            // Invalid JWT signature
            System.out.println("invalid signature");
            response.getWriter().write("Invalid signature");
        } catch (MalformedJwtException e) {
            // Invalid JWT token
            System.out.println("invalid token");
            response.getWriter().write("Invalid token");
        } catch (ExpiredJwtException e) {
            // Expired JWT token
            System.out.println("expired token");
            response.getWriter().write("Expired token");
        } catch (UnsupportedJwtException e) {
            // Unsupported JWT token
            System.out.println("unsupported token");
            response.getWriter().write("Unsupported token");
        } catch (IllegalArgumentException e) {
            // JWT claims string is empty
            System.out.println("string is empty");
            response.getWriter().write("token is empty");
        }
        response.getWriter().close();
        return false;
    }

}
