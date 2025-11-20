package com.petconnect.pets.infrastructure.driver_adapters.jpa_repository.JwtDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JwtUserDetails implements UserDetails {
    @Getter
    private Long id;
    private String username;
    @Getter
    private List<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> {
                    if (!role.startsWith("ROLE_")) {
                        return new SimpleGrantedAuthority("ROLE_" + role);
                    }
                    return new SimpleGrantedAuthority(role);
                })
                .collect(Collectors.toList()); // Usar collect aquí también
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
