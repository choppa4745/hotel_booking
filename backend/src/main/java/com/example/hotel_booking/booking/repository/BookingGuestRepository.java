package com.example.hotel_booking.booking.repository;


import com.example.hotel_booking.booking.entity.BookingGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingGuestRepository extends JpaRepository<BookingGuest, UUID> {

    List<BookingGuest> findByBookingBookingId(UUID bookingId);

    List<BookingGuest> findByGuestGuestId(UUID guestId);

    void deleteByBookingBookingId(UUID bookingId);
}