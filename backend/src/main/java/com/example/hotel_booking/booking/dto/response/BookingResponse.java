package com.example.hotel_booking.booking.dto.response;


import com.example.hotel_booking.room.dto.response.RoomResponse;
import com.example.hotel_booking.user.dto.response.GuestResponse;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class BookingResponse {
    private UUID bookingId;
    private GuestResponse guest;
    private RoomResponse room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<GuestResponse> additionalGuests;
}