package com.example.hotel_booking.security.service;

import com.example.hotel_booking.user.entity.User;
import com.example.hotel_booking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.example.hotel_booking.security.SecurityUserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));

        // hasRole("ADMIN") ожидает authority = "ROLE_ADMIN"
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new SecurityUserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                Boolean.TRUE.equals(user.getIsActive()),
                user.getRole(),
                user.getEmployeeId(),
                user.getGuestId(),
                authorities
        );
    }
}
