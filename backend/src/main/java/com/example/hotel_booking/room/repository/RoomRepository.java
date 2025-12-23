package com.example.hotel_booking.room.repository;


import com.example.hotel_booking.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findByIsAvailableTrue();
    boolean existsByRoomNumber(String roomNumber);
}