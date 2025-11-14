package com.example.hotel_booking.user.service;



import com.example.hotel_booking.user.dto.request.GuestRequest;
import com.example.hotel_booking.user.dto.response.GuestResponse;
import com.example.hotel_booking.user.entity.Guest;

import java.util.List;
import java.util.UUID;

public interface GuestService {
    List<GuestResponse> getAllGuests();
    GuestResponse getGuestById(UUID id);
    GuestResponse getGuestByEmail(String email);
    GuestResponse getGuestByPassportNumber(String passportNumber);
    GuestResponse createGuest(GuestRequest request);
    GuestResponse updateGuest(UUID id, GuestRequest request);
    void deleteGuest(UUID id);
    boolean existsByEmail(String email);
    boolean existsByPassportNumber(String passportNumber);

    Guest getGuestEntityById(UUID id);
}