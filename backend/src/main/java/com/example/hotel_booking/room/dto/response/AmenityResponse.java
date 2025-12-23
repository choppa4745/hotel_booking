package com.example.hotel_booking.room.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AmenityResponse {
    private UUID amenityId;
    private String amenityName;
    private String description;
    private LocalDateTime createdAt;
}
