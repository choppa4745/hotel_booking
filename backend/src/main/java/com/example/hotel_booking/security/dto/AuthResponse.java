package com.example.hotel_booking.auth.dto;

import com.example.hotel_booking.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String username;
    private Role role;
    private UUID employeeId;
    private UUID guestId;
}
