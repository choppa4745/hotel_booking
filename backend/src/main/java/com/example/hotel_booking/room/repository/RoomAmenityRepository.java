package com.example.hotel_booking.room.repository;

import com.example.hotel_booking.room.entity.RoomAmenity;
import com.example.hotel_booking.room.entity.RoomAmenityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomAmenityRepository extends JpaRepository<RoomAmenity, RoomAmenityId> {
    List<RoomAmenity> findByRoomRoomId(UUID roomId);
    void deleteByRoomRoomId(UUID roomId);
}
