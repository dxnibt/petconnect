package com.petconnect.auth.infraestructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtValidation {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;


        // Generar token
        public String generateToken(Long userId, String role) {
            return Jwts.builder()
                    .setSubject(userId.toString())
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
        }

        // Obtener userId del token
        public Long getUserId(String token) {
            Claims claims = parseToken(token);
            return Long.parseLong(claims.getSubject());
        }

        // Obtener rol del token
        public String getRole(String token) {
            Claims claims = parseToken(token);
            return claims.get("roles", String.class);
        }

        // Validar token
        public boolean validateToken(String token) {
            try {
                parseToken(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        private Claims parseToken(String token) {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }
}


