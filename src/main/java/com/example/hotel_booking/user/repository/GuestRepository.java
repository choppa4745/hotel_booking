package com.example.hotel_booking.user.repository;

import com.example.hotel_booking.user.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GuestRepository extends JpaRepository<Guest, UUID> {
    Optional<Guest> findByEmail(String email);
    Optional<Guest> findByPhone(String phone);
    Optional<Guest> findByPassportNumber(String passportNumber);

}

