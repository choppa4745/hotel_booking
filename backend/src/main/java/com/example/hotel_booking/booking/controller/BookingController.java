package com.example.hotel_booking.booking.controller;

import com.example.hotel_booking.booking.dto.request.BookingRequest;
import com.example.hotel_booking.booking.dto.response.BookingResponse;
import com.example.hotel_booking.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Booking Management", description = "APIs for managing hotel bookings")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Get all bookings", description = "Retrieve a list of all bookings")
    @GetMapping
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @Operation(summary = "Get booking by ID", description = "Retrieve a specific booking by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(
            @Parameter(description = "Booking ID", required = true)
            @PathVariable UUID id) {
        try {
            BookingResponse booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create new booking", description = "Create a new booking with availability check")
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Parameter(description = "Booking details", required = true)
            @Valid @RequestBody BookingRequest request) {
        try {
            BookingResponse createdBooking = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get bookings by guest", description = "Retrieve all bookings for a specific guest")
    @GetMapping("/guest/{guestId}")
    public List<BookingResponse> getBookingsByGuest(
            @Parameter(description = "Guest ID", required = true)
            @PathVariable UUID guestId) {
        return bookingService.getBookingsByGuest(guestId);
    }

    @Operation(summary = "Get bookings by room", description = "Retrieve all bookings for a specific room")
    @GetMapping("/room/{roomId}")
    public List<BookingResponse> getBookingsByRoom(
            @Parameter(description = "Room ID", required = true)
            @PathVariable UUID roomId) {
        return bookingService.getBookingsByRoom(roomId);
    }

    @Operation(summary = "Get bookings by status", description = "Retrieve bookings by status (CONFIRMED, CHECKED_IN, etc.)")
    @GetMapping("/status/{status}")
    public List<BookingResponse> getBookingsByStatus(
            @Parameter(description = "Booking status", required = true, example = "CONFIRMED")
            @PathVariable String status) {
        return bookingService.getBookingsByStatus(status);
    }

    @Operation(summary = "Get active bookings", description = "Retrieve all currently active bookings")
    @GetMapping("/active")
    public List<BookingResponse> getActiveBookings() {
        return bookingService.getActiveBookings();
    }

//    @Operation(summary = "Confirm booking", description = "Confirm a pending booking")
//    @PatchMapping("/{id}/confirm")
//    public ResponseEntity<BookingResponse> confirmBooking(
//            @Parameter(description = "Booking ID", required = true)
//            @PathVariable UUID id) {
//        try {
//            BookingResponse confirmedBooking = bookingService.confirmBooking(id);
//            return ResponseEntity.ok(confirmedBooking);
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @Operation(summary = "Check-in booking", description = "Check-in a confirmed booking")
    @PatchMapping("/{id}/check-in")
    public ResponseEntity<BookingResponse> checkInBooking(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(bookingService.checkInBooking(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Check-out booking", description = "Check-out from a booking")
    @PatchMapping("/{id}/check-out")
    public ResponseEntity<BookingResponse> checkOutBooking(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(bookingService.checkOutBooking(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Cancel booking", description = "Cancel a booking")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(
            @Parameter(description = "Booking ID", required = true)
            @PathVariable UUID id) {
        try {
            bookingService.cancelBooking(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get my bookings (CLIENT)")
    @GetMapping("/my")
    public List<BookingResponse> getMyBookings() {
        UUID guestId = com.example.hotel_booking.security.SecurityUtils.currentGuestId();
        return bookingService.getBookingsByGuest(guestId);
    }


    @Operation(summary = "Check room availability", description = "Check if a room is available for given dates")
    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkRoomAvailability(
            @Parameter(description = "Room ID", required = true)
            @RequestParam UUID roomId,
            @Parameter(description = "Check-in date (YYYY-MM-DD)", required = true, example = "2024-01-01")
            @RequestParam String checkInDate,
            @Parameter(description = "Check-out date (YYYY-MM-DD)", required = true, example = "2024-01-05")
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