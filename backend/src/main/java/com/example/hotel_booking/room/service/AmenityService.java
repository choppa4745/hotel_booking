package com.example.hotel_booking.room.service;

import com.example.hotel_booking.room.dto.request.AmenityRequest;
import com.example.hotel_booking.room.dto.response.AmenityResponse;

import java.util.List;
import java.util.UUID;

public interface AmenityService {
    List<AmenityResponse> getAll();
    AmenityResponse getById(UUID id);
    AmenityResponse create(AmenityRequest request);
    AmenityResponse update(UUID id, AmenityRequest request);
    void delete(UUID id);
}
