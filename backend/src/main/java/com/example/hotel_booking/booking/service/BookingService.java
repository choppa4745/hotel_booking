package com.example.hotel_booking.booking.service;



import com.example.hotel_booking.booking.dto.request.BookingRequest;
import com.example.hotel_booking.booking.dto.response.BookingResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingService {
    List<BookingResponse> getAllBookings();
    BookingResponse getBookingById(UUID id);
    List<BookingResponse> getBookingsByGuest(UUID guestId);
    List<BookingResponse> getBookingsByRoom(UUID roomId);
    List<BookingResponse> getBookingsByStatus(String status);
    List<BookingResponse> getActiveBookings();
    BookingResponse createBooking(BookingRequest request);
    BookingResponse updateBooking(UUID id, BookingRequest request);
    void cancelBooking(UUID id);
    BookingResponse confirmBooking(UUID id);
    BookingResponse checkInBooking(UUID bookingId);
    BookingResponse checkOutBooking(UUID bookingId);
    boolean isRoomAvailable(UUID roomId, LocalDate checkInDate, LocalDate checkOutDate);
    List<BookingResponse> findConflictingBookings(UUID roomId, LocalDate checkInDate, LocalDate checkOutDate);
}