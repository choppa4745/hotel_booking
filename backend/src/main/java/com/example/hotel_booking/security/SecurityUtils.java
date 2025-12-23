package com.example.hotel_booking.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtils {
    private SecurityUtils() {}

    public static SecurityUserPrincipal principal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUserPrincipal p)) {
            throw new IllegalStateException("No authenticated user");
        }
        return p;
    }
    public static String currentAuthorityOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        return auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .findFirst()
                .orElse(null);
    }


    public static UUID currentGuestId() { return principal().getGuestId(); }
    public static UUID currentEmployeeId() { return principal().getEmployeeId(); }
}
