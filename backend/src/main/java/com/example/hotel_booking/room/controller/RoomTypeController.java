package com.example.hotel_booking.room.controller;


import com.example.hotel_booking.room.dto.request.RoomTypeRequest;
import com.example.hotel_booking.room.dto.response.RoomTypeResponse;
import com.example.hotel_booking.room.service.RoomTypeService;
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
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
@Tag(name = "Room Type Management", description = "APIs for managing room types")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @Operation(summary = "Get all room types", description = "Retrieve a list of all room types")
    @GetMapping
    public List<RoomTypeResponse> getAllRoomTypes() {
        return roomTypeService.getAllRoomTypes();
    }

    @Operation(summary = "Get room type by ID", description = "Retrieve a specific room type by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeResponse> getRoomTypeById(
            @Parameter(description = "Room Type ID", required = true)
            @PathVariable UUID id) {
        try {
            RoomTypeResponse roomType = roomTypeService.getRoomTypeById(id);
            return ResponseEntity.ok(roomType);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create new room type", description = "Create a new room type")
    @PostMapping
    public ResponseEntity<RoomTypeResponse> createRoomType(
            @Parameter(description = "Room type details", required = true)
            @Valid @RequestBody RoomTypeRequest request) {
        try {
            RoomTypeResponse createdRoomType = roomTypeService.createRoomType(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoomType);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update room type", description = "Update an existing room type")
    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeResponse> updateRoomType(
            @Parameter(description = "Room Type ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated room type details", required = true)
            @Valid @RequestBody RoomTypeRequest request) {
        try {
            RoomTypeResponse updatedRoomType = roomTypeService.updateRoomType(id, request);
            return ResponseEntity.ok(updatedRoomType);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete room type", description = "Delete a room type by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(
            @Parameter(description = "Room Type ID", required = true)
            @PathVariable UUID id) {
        try {
            roomTypeService.deleteRoomType(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get room type by name", description = "Retrieve a room type by its name")
    @GetMapping("/name/{typeName}")
    public ResponseEntity<RoomTypeResponse> getRoomTypeByName(
            @Parameter(description = "Room type name", required = true, example = "Deluxe")
            @PathVariable String typeName) {
        try {
            RoomTypeResponse roomType = roomTypeService.getRoomTypeByName(typeName);
            return ResponseEntity.ok(roomType);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}