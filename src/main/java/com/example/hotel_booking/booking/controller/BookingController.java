package com.example.hotel_booking.booking.controller;


import com.example.hotel_booking.booking.dto.request.BookingRequest;
import com.example.hotel_booking.booking.dto.response.BookingResponse;
import com.example.hotel_booking.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable UUID id) {
        try {
            BookingResponse booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        try {
            BookingResponse createdBooking = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/guest/{guestId}")
    public List<BookingResponse> getBookingsByGuest(@PathVariable UUID guestId) {
        return bookingService.getBookingsByGuest(guestId);
    }

    @GetMapping("/room/{roomId}")
    public List<BookingResponse> getBookingsByRoom(@PathVariable UUID roomId) {
        return bookingService.getBookingsByRoom(roomId);
    }

    @GetMapping("/status/{status}")
    public List<BookingResponse> getBookingsByStatus(@PathVariable String status) {
        return bookingService.getBookingsByStatus(status);
    }

    @GetMapping("/active")
    public List<BookingResponse> getActiveBookings() {
        return bookingService.getActiveBookings();
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<BookingResponse> confirmBooking(@PathVariable UUID id) {
        try {
            BookingResponse confirmedBooking = bookingService.confirmBooking(id);
            return ResponseEntity.ok(confirmedBooking);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/check-in")
    public ResponseEntity<BookingResponse> checkInBooking(
            @PathVariable UUID id,
            @RequestParam UUID employeeId) {
        try {
            BookingResponse checkedInBooking = bookingService.checkInBooking(id, employeeId);
            return ResponseEntity.ok(checkedInBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/check-out")
    public ResponseEntity<BookingResponse> checkOutBooking(
            @PathVariable UUID id,
            @RequestParam UUID employeeId) {
        try {
            BookingResponse checkedOutBooking = bookingService.checkOutBooking(id, employeeId);
            return ResponseEntity.ok(checkedOutBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable UUID id) {
        try {
            bookingService.cancelBooking(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkRoomAvailability(
            @RequestParam UUID roomId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate) {
        try {
            boolean isAvailable = bookingService.isRoomAvailable(
                    roomId,
                    LocalDate.parse(checkInDate),
                    LocalDate.parse(checkOutDate)
            );
            return ResponseEntity.ok(isAvailable);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}