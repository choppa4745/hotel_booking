package com.example.hotel_booking.security.dto;

import com.example.hotel_booking.user.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class RegisterRequest {
    @NotBlank private String username;
    @Email @NotBlank private String email;
    @NotBlank @Size(min = 8) private String password;

    @NotNull private Role role;

    private UUID guestId;
    private UUID employeeId;
}
