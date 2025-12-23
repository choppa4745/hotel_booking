package com.example.hotel_booking.room.dto.response;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class RoomResponse {
    private UUID roomId;
    private String roomNumber;
    private RoomTypeResponse roomType;
    private Integer floor;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private List<AmenityResponse> amenities;

}