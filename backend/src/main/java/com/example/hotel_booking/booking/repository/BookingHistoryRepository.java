package com.example.hotel_booking.booking.repository;

import com.example.hotel_booking.booking.entity.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface BookingHistoryRepository extends JpaRepository<BookingHistory, UUID> {
    List<BookingHistory> findByBookingIdOrderByChangedAtDesc(UUID bookingId);
}