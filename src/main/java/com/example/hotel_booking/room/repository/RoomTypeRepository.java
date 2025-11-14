package com.example.hotel_booking.room.repository;



import com.example.hotel_booking.room.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, UUID> {
    Optional<RoomType> findByTypeName(String typeName);
    boolean existsByTypeName(String typeName);
}