package com.example.hotel_booking.room.service;



import com.example.hotel_booking.room.dto.request.RoomRequest;
import com.example.hotel_booking.room.dto.response.RoomResponse;
import com.example.hotel_booking.room.entity.Room;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    List<RoomResponse> getAllRooms();
    List<RoomResponse> getAvailableRooms();
    RoomResponse getRoomById(UUID id);
    RoomResponse getRoomByNumber(String roomNumber);
    RoomResponse createRoom(RoomRequest request);
    RoomResponse updateRoom(UUID id, RoomRequest request);
    void deleteRoom(UUID id);
    RoomResponse updateRoomAvailability(UUID id, boolean isAvailable);
    boolean existsByRoomNumber(String roomNumber);

    Room getRoomEntityById(UUID id);
}