package com.example.hotel_booking.user.dto.request;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GuestRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String phone;

    private String passportNumber;

    @PastOrPresent(message = "Date of birth must be in the past or present")
    private LocalDate dateOfBirth;
}