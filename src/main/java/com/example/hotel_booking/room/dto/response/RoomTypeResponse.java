package com.example.hotel_booking.room.dto.response;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RoomTypeResponse {
    private UUID typeId;
    private String typeName;
    private BigDecimal basePrice;
    private Integer maxGuests;
    private String description;
    private LocalDateTime createdAt;
}