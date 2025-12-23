package com.example.hotel_booking.room.service.impl;



import com.example.hotel_booking.room.dto.request.RoomTypeRequest;
import com.example.hotel_booking.room.dto.response.RoomTypeResponse;
import com.example.hotel_booking.room.entity.RoomType;
import com.example.hotel_booking.room.repository.RoomTypeRepository;
import com.example.hotel_booking.room.service.RoomTypeService;
import com.example.hotel_booking.shared.exception.ResourceNotFoundException;
import com.example.hotel_booking.shared.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeResponse> getAllRoomTypes() {
        log.info("Getting all room types");
        return roomTypeRepository.findAll()
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomTypeResponse getRoomTypeById(UUID id) {
        log.info("Getting room type by id: {}", id);
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomType", "id", id));
        return roomMapper.toResponse(roomType);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomTypeResponse getRoomTypeByName(String typeName) {
        log.info("Getting room type by name: {}", typeName);
        RoomType roomType = roomTypeRepository.findByTypeName(typeName)
                .orElseThrow(() -> new ResourceNotFoundException("RoomType", "name", typeName));
        return roomMapper.toResponse(roomType);
    }

    @Override
    public RoomTypeResponse createRoomType(RoomTypeRequest request) {
        log.info("Creating new room type: {}", request.getTypeName());

        if (roomTypeRepository.existsByTypeName(request.getTypeName())) {
            throw new RuntimeException("Room type with name " + request.getTypeName() + " already exists");
        }

        RoomType roomType = roomMapper.toEntity(request);
        RoomType savedRoomType = roomTypeRepository.save(roomType);

        return roomMapper.toResponse(savedRoomType);
    }

    @Override
    @Transactional
    public RoomTypeResponse updateRoomType(UUID id, RoomTypeRequest request) {
        log.info("Updating room type with id: {}", id);

        RoomType existing = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomType", "id", id));

        existing.setTypeName(request.getTypeName());
        existing.setBasePrice(request.getBasePrice());
        existing.setMaxGuests(request.getMaxGuests());
        existing.setDescription(request.getDescription());

        RoomType saved = roomTypeRepository.save(existing);

        RoomTypeResponse response = new RoomTypeResponse();
        response.setTypeId(saved.getTypeId());
        response.setTypeName(saved.getTypeName());
        response.setBasePrice(saved.getBasePrice());
        response.setMaxGuests(saved.getMaxGuests());
        response.setDescription(saved.getDescription());
        response.setCreatedAt(saved.getCreatedAt());

        return response;
    }


    @Override
    public void deleteRoomType(UUID id) {
        log.info("Deleting room type with id: {}", id);

        if (!roomTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("RoomType", "id", id);
        }

        roomTypeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTypeName(String typeName) {
        return roomTypeRepository.existsByTypeName(typeName);
    }
}