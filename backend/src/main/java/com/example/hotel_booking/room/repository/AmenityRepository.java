package com.example.hotel_booking.room.repository;

import com.example.hotel_booking.room.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, UUID> {
    Optional<Amenity> findByAmenityName(String amenityName);
    boolean existsByAmenityName(String amenityName);
}
