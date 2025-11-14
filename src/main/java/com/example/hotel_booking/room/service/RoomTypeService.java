package com.example.hotel_booking.room.service;


import com.example.hotel_booking.room.dto.request.RoomTypeRequest;
import com.example.hotel_booking.room.dto.response.RoomTypeResponse;

import java.util.List;
import java.util.UUID;

public interface RoomTypeService {
    List<RoomTypeResponse> getAllRoomTypes();
    RoomTypeResponse getRoomTypeById(UUID id);
    RoomTypeResponse getRoomTypeByName(String typeName);
    RoomTypeResponse createRoomType(RoomTypeRequest request);
    RoomTypeResponse updateRoomType(UUID id, RoomTypeRequest request);
    void deleteRoomType(UUID id);
    boolean existsByTypeName(String typeName);
}