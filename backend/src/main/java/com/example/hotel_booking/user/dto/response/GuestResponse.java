package com.example.hotel_booking.user.dto.response;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class GuestResponse {
    private UUID guestId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String passportNumber;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
}