package com.example.hotel_booking.audit.controller;

import com.example.hotel_booking.booking.entity.BookingHistory;
import com.example.hotel_booking.booking.repository.BookingHistoryRepository;
import com.example.hotel_booking.room.entity.PriceHistory;
import com.example.hotel_booking.room.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final BookingHistoryRepository bookingHistoryRepository;
    private final PriceHistoryRepository priceHistoryRepository;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bookings/{bookingId}")
    public List<BookingHistory> bookingHistory(@PathVariable UUID bookingId) {
        return bookingHistoryRepository.findByBookingIdOrderByChangedAtDesc(bookingId);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/room-types/{roomTypeId}/prices")
    public List<PriceHistory> priceHistory(@PathVariable UUID roomTypeId) {
        return priceHistoryRepository.findByRoomTypeIdOrderByChangedAtDesc(roomTypeId);
    }
}
