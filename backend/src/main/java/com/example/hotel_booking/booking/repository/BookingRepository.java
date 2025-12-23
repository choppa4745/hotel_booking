package com.example.hotel_booking.booking.repository;

import com.example.hotel_booking.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByGuestGuestId(UUID guestId);

    List<Booking> findByRoomRoomId(UUID roomId);

    List<Booking> findByStatus(String status);

    @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId AND b.status IN ('CONFIRMED', 'CHECKED_IN') " +
            "AND (b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate)")
    List<Booking> findConflictingBookings(@Param("roomId") UUID roomId,
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate);

    @Query("SELECT b FROM Booking b WHERE b.status IN ('CONFIRMED', 'CHECKED_IN') " +
            "AND CURRENT_DATE BETWEEN b.checkInDate AND b.checkOutDate")
    List<Booking> findActiveBookings();

    List<Booking> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);

    List<Booking> findByCheckOutDateBetween(LocalDate startDate, LocalDate endDate);
}