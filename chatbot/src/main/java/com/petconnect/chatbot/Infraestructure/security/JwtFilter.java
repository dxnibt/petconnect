package com.petconnect.chatbot.Infraestructure.security;

import com.petconnect.chatbot.Infraestructure.driver_adapters.jpa_repository.JwtDto.JwtUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(token)
                        .getBody();

                String userId = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);

                if (userId != null && roles != null) {
                    // Usar List<GrantedAuthority> explícitamente
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(role -> {
                                // Si el rol no tiene el prefijo, agregarlo
                                if (!role.startsWith("ROLE_")) {
                                    return new SimpleGrantedAuthority("ROLE_" + role);
                                }
                                return new SimpleGrantedAuthority(role);
                            })
                            .collect(Collectors.toList()); // Usar collect en lugar de toList()

                    JwtUserDetails userDetails = new JwtUserDetails(Long.parseLong(userId), userId, roles);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(auth);

                    // Logs para depuración
                    System.out.println("JWT Filter - User ID: " + userId);
                    System.out.println("JWT Filter - Roles from token: " + roles);
                    System.out.println("JWT Filter - Authorities: " + authorities);
                }

            } catch (Exception e) {
                System.err.println("JWT Filter Error: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        } else {
            System.out.println("JWT Filter: No Authorization header found");
        }

        filterChain.doFilter(request, response);
    }
}
