package com.example.hotel_booking.booking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class BookingRequest {

    @NotNull(message = "Guest ID is required")
    private UUID guestId;

    @NotNull(message = "Room ID is required")
    private UUID roomId;

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in date must be today or in the future")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;

    private List<UUID> additionalGuestIds;

//    @AssertTrue(message = "Check-out date must be after check-in date")
//    public boolean isCheckOutDateValid() {
//        return checkOutDate != null && checkInDate != null && checkOutDate.isAfter(checkInDate);
//    }
}
