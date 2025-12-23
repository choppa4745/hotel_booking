package com.example.hotel_booking.room.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RoomRequest {
    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Room type ID is required")
    private UUID typeId;

    @NotNull(message = "Floor is required")
    @Positive(message = "Floor must be positive")
    private Integer floor;

    private Boolean isAvailable = true;

    private List<UUID> amenityIds;

}