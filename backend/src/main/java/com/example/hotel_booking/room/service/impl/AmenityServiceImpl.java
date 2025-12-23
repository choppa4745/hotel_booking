package com.example.hotel_booking.room.service.impl;

import com.example.hotel_booking.room.dto.request.AmenityRequest;
import com.example.hotel_booking.room.dto.response.AmenityResponse;
import com.example.hotel_booking.room.entity.Amenity;
import com.example.hotel_booking.room.repository.AmenityRepository;
import com.example.hotel_booking.room.service.AmenityService;
import com.example.hotel_booking.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AmenityResponse> getAll() {
        return amenityRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AmenityResponse getById(UUID id) {
        Amenity a = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity", "id", id));
        return toResponse(a);
    }

    @Override
    public AmenityResponse create(AmenityRequest request) {
        if (amenityRepository.existsByAmenityName(request.getAmenityName())) {
            throw new RuntimeException("Amenity with name " + request.getAmenityName() + " already exists");
        }
        Amenity a = new Amenity();
        a.setAmenityName(request.getAmenityName());
        a.setDescription(request.getDescription());
        return toResponse(amenityRepository.save(a));
    }

    @Override
    public AmenityResponse update(UUID id, AmenityRequest request) {
        Amenity a = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity", "id", id));

        // если меняем имя — проверяем уникальность
        if (!a.getAmenityName().equals(request.getAmenityName())
                && amenityRepository.existsByAmenityName(request.getAmenityName())) {
            throw new RuntimeException("Amenity with name " + request.getAmenityName() + " already exists");
        }

        a.setAmenityName(request.getAmenityName());
        a.setDescription(request.getDescription());
        return toResponse(amenityRepository.save(a));
    }

    @Override
    public void delete(UUID id) {
        if (!amenityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Amenity", "id", id);
        }
        amenityRepository.deleteById(id);
    }

    private AmenityResponse toResponse(Amenity a) {
        AmenityResponse r = new AmenityResponse();
        r.setAmenityId(a.getAmenityId());
        r.setAmenityName(a.getAmenityName());
        r.setDescription(a.getDescription());
        r.setCreatedAt(a.getCreatedAt());
        return r;
    }
}
