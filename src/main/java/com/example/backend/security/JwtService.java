package com.example.backend.security;

import com.example.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

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
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60) ) //bir haftalik
                .compact();
    }


    private Key generateSecretKey() {
        byte[] bytes = "O'zbekistonvatanimmanimgullayashnahuro'zbekistonshiftacademyengzroquvmarkaz".getBytes();
        return Keys.hmacShaKeyFor(bytes);
    }


    public String extractSubjectFromJWT(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(generateSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public Boolean isValid(String token) {
            Jwts
                    .parserBuilder()
                    .setSigningKey(generateSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject();
            return true;
    }

}
