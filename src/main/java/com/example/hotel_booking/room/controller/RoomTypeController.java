package com.example.hotel_booking.room.controller;


import com.example.hotel_booking.room.dto.request.RoomTypeRequest;
import com.example.hotel_booking.room.dto.response.RoomTypeResponse;
import com.example.hotel_booking.room.service.RoomTypeService;
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
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping
    public List<RoomTypeResponse> getAllRoomTypes() {
        return roomTypeService.getAllRoomTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeResponse> getRoomTypeById(@PathVariable UUID id) {
        try {
            RoomTypeResponse roomType = roomTypeService.getRoomTypeById(id);
            return ResponseEntity.ok(roomType);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<RoomTypeResponse> createRoomType(@Valid @RequestBody RoomTypeRequest request) {
        try {
            RoomTypeResponse createdRoomType = roomTypeService.createRoomType(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoomType);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeResponse> updateRoomType(
            @PathVariable UUID id,
            @Valid @RequestBody RoomTypeRequest request) {
        try {
            RoomTypeResponse updatedRoomType = roomTypeService.updateRoomType(id, request);
            return ResponseEntity.ok(updatedRoomType);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable UUID id) {
        try {
            roomTypeService.deleteRoomType(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{typeName}")
    public ResponseEntity<RoomTypeResponse> getRoomTypeByName(@PathVariable String typeName) {
        try {
            RoomTypeResponse roomType = roomTypeService.getRoomTypeByName(typeName);
            return ResponseEntity.ok(roomType);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}