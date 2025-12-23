package com.example.hotel_booking.user.controller;

import com.example.hotel_booking.user.dto.request.GuestRequest;
import com.example.hotel_booking.user.dto.response.GuestResponse;
import com.example.hotel_booking.user.service.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Guest Management", description = "APIs for managing hotel guests")
public class GuestController {

    private final GuestService guestService;

    @Operation(summary = "Get all guests", description = "Retrieve a list of all guests")
    @GetMapping
    public List<GuestResponse> getAllGuests() {
        return guestService.getAllGuests();
    }

    @Operation(summary = "Get guest by ID", description = "Retrieve a specific guest by ID")
    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getGuestById(
            @Parameter(description = "Guest ID", required = true)
            @PathVariable UUID id) {
        try {
            GuestResponse guest = guestService.getGuestById(id);
            return ResponseEntity.ok(guest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create new guest", description = "Create a new guest profile")
    @PostMapping
    public ResponseEntity<GuestResponse> createGuest(
            @Parameter(description = "Guest details", required = true)
            @Valid @RequestBody GuestRequest request) {
        try {
            GuestResponse createdGuest = guestService.createGuest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGuest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update guest", description = "Update an existing guest profile")
    @PutMapping("/{id}")
    public ResponseEntity<GuestResponse> updateGuest(
            @Parameter(description = "Guest ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated guest details", required = true)
            @Valid @RequestBody GuestRequest request) {
        try {
            GuestResponse updatedGuest = guestService.updateGuest(id, request);
            return ResponseEntity.ok(updatedGuest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete guest", description = "Delete a guest by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(
            @Parameter(description = "Guest ID", required = true)
            @PathVariable UUID id) {
        try {
            guestService.deleteGuest(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get guest by email", description = "Retrieve a guest by email address")
    @GetMapping("/email/{email}")
    public ResponseEntity<GuestResponse> getGuestByEmail(
            @Parameter(description = "Email address", required = true, example = "guest@example.com")
            @PathVariable String email) {
        try {
            GuestResponse guest = guestService.getGuestByEmail(email);
            return ResponseEntity.ok(guest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get guest by passport", description = "Retrieve a guest by passport number")
    @GetMapping("/passport/{passportNumber}")
    public ResponseEntity<GuestResponse> getGuestByPassportNumber(
            @Parameter(description = "Passport number", required = true, example = "AB1234567")
            @PathVariable String passportNumber) {
        try {
            GuestResponse guest = guestService.getGuestByPassportNumber(passportNumber);
            return ResponseEntity.ok(guest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}