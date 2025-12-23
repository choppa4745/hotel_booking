package com.example.hotel_booking.room.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AmenityRequest {
    @NotBlank(message = "Amenity name is required")
    private String amenityName;
    private String description;
}
