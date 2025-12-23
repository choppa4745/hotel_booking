package com.example.hotel_booking.room.controller;


import com.example.hotel_booking.room.dto.request.RoomRequest;
import com.example.hotel_booking.room.dto.response.RoomResponse;
import com.example.hotel_booking.room.service.RoomService;
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
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Tag(name = "Room Management", description = "APIs for managing hotel rooms")
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "Get all rooms", description = "Retrieve a list of all rooms")
    @GetMapping
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    @Operation(summary = "Get room by ID", description = "Retrieve a specific room by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(
            @Parameter(description = "Room ID", required = true)
            @PathVariable UUID id) {
        try {
            RoomResponse room = roomService.getRoomById(id);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create new room", description = "Create a new room")
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {
        RoomResponse createdRoom = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable UUID id,
            @Valid @RequestBody RoomRequest request
    ) {
        RoomResponse updatedRoom = roomService.updateRoom(id, request);
        return ResponseEntity.ok(updatedRoom);
    }

    @Operation(summary = "Delete room", description = "Delete a room by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @Parameter(description = "Room ID", required = true)
            @PathVariable UUID id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get available rooms", description = "Retrieve all available rooms")
    @GetMapping("/available")
    public List<RoomResponse> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }

    @Operation(summary = "Get room by number", description = "Retrieve a room by its number")
    @GetMapping("/number/{roomNumber}")
    public ResponseEntity<RoomResponse> getRoomByNumber(
            @Parameter(description = "Room number", required = true, example = "101")
            @PathVariable String roomNumber) {
        try {
            RoomResponse room = roomService.getRoomByNumber(roomNumber);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update room availability", description = "Update room availability status")
    @PatchMapping("/{id}/availability")
    public ResponseEntity<RoomResponse> updateRoomAvailability(
            @Parameter(description = "Room ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Availability status", required = true)
            @RequestParam boolean available) {
        try {
            RoomResponse updatedRoom = roomService.updateRoomAvailability(id, available);
            return ResponseEntity.ok(updatedRoom);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}