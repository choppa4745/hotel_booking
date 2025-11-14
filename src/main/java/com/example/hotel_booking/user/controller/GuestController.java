package com.example.hotel_booking.user.controller;


import com.example.hotel_booking.user.dto.request.GuestRequest;
import com.example.hotel_booking.user.dto.response.GuestResponse;
import com.example.hotel_booking.user.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @GetMapping
    public List<GuestResponse> getAllGuests() {
        return guestService.getAllGuests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getGuestById(@PathVariable UUID id) {
        try {
            GuestResponse guest = guestService.getGuestById(id);
            return ResponseEntity.ok(guest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GuestResponse> createGuest(@Valid @RequestBody GuestRequest request) {
        try {
            GuestResponse createdGuest = guestService.createGuest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGuest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestResponse> updateGuest(
            @PathVariable UUID id,
            @Valid @RequestBody GuestRequest request) {
        try {
            GuestResponse updatedGuest = guestService.updateGuest(id, request);
            return ResponseEntity.ok(updatedGuest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable UUID id) {
        try {
            guestService.deleteGuest(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<GuestResponse> getGuestByEmail(@PathVariable String email) {
        try {
            GuestResponse guest = guestService.getGuestByEmail(email);
            return ResponseEntity.ok(guest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/passport/{passportNumber}")
    public ResponseEntity<GuestResponse> getGuestByPassportNumber(@PathVariable String passportNumber) {
        try {
            GuestResponse guest = guestService.getGuestByPassportNumber(passportNumber);
            return ResponseEntity.ok(guest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}