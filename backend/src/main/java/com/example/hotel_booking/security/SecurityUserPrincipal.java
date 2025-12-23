package com.example.hotel_booking.security;

import com.example.hotel_booking.user.entity.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
public class SecurityUserPrincipal implements UserDetails {

    private final UUID id;
    private final String username;
    private final String passwordHash;
    private final boolean enabled;

    private final Role role;
    private final UUID employeeId;
    private final UUID guestId;

    private final Collection<? extends GrantedAuthority> authorities;

    public SecurityUserPrincipal(
            UUID id,
            String username,
            String passwordHash,
            boolean enabled,
            Role role,
            UUID employeeId,
            UUID guestId,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.role = role;
        this.employeeId = employeeId;
        this.guestId = guestId;
        this.authorities = authorities;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return passwordHash; }
    @Override public String getUsername() { return username; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}
