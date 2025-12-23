package com.example.hotel_booking.room.controller;

import com.example.hotel_booking.room.dto.request.AmenityRequest;
import com.example.hotel_booking.room.dto.response.AmenityResponse;
import com.example.hotel_booking.room.service.AmenityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenityController {

    private final AmenityService amenityService;

    @GetMapping
    public List<AmenityResponse> getAll() {
        return amenityService.getAll();
    }

    @GetMapping("/{id}")
    public AmenityResponse getById(@PathVariable UUID id) {
        return amenityService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AmenityResponse create(@Valid @RequestBody AmenityRequest request) {
        return amenityService.create(request);
    }

    @PutMapping("/{id}")
    public AmenityResponse update(@PathVariable UUID id, @Valid @RequestBody AmenityRequest request) {
        return amenityService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        amenityService.delete(id);
    }
}
