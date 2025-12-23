package com.example.hotel_booking.room.service.impl;


import com.example.hotel_booking.room.dto.request.RoomRequest;
import com.example.hotel_booking.room.dto.response.RoomResponse;
import com.example.hotel_booking.room.entity.*;
import com.example.hotel_booking.room.repository.AmenityRepository;
import com.example.hotel_booking.room.repository.RoomAmenityRepository;
import com.example.hotel_booking.room.repository.RoomRepository;
import com.example.hotel_booking.room.repository.RoomTypeRepository;
import com.example.hotel_booking.room.service.RoomService;
import com.example.hotel_booking.shared.exception.ResourceNotFoundException;
import com.example.hotel_booking.shared.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomMapper roomMapper;
    private final AmenityRepository amenityRepository;
    private final RoomAmenityRepository roomAmenityRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        log.info("Getting all rooms");
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAvailableRooms() {
        log.info("Getting available rooms");
        return roomRepository.findByIsAvailableTrue()
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse getRoomById(UUID id) {
        log.info("Getting room by id: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        return roomMapper.toResponse(room);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse getRoomByNumber(String roomNumber) {
        log.info("Getting room by number: {}", roomNumber);
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomNumber", roomNumber));
        return roomMapper.toResponse(room);
    }

    @Override
    public RoomResponse createRoom(RoomRequest request) {
        log.info("Creating new room: {}", request.getRoomNumber());

        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new RuntimeException("Room with number " + request.getRoomNumber() + " already exists");
        }

        RoomType roomType = roomTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("RoomType", "id", request.getTypeId()));

        Room room = roomMapper.toEntity(request);
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(roomType);
        room.setIsAvailable(request.getIsAvailable());
        syncAmenities(room, request.getAmenityIds());

        Room savedRoom = roomRepository.save(room);
        return roomMapper.toResponse(savedRoom);
    }



    @Override
    public RoomResponse updateRoom(UUID id, RoomRequest request) {
        log.info("Updating room with id: {}", id);

        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));

        RoomType roomType = roomTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("RoomType", "id", request.getTypeId()));

        existingRoom.setRoomNumber(request.getRoomNumber());
        existingRoom.setRoomType(roomType);
        existingRoom.setFloor(request.getFloor());
        existingRoom.setIsAvailable(request.getIsAvailable());
        syncAmenities(existingRoom, request.getAmenityIds());

        Room updatedRoom = roomRepository.save(existingRoom);
        return roomMapper.toResponse(updatedRoom);
    }

    @Override
    public void deleteRoom(UUID id) {
        log.info("Deleting room with id: {}", id);

        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room", "id", id);
        }

        roomRepository.deleteById(id);
    }

    @Override
    public RoomResponse updateRoomAvailability(UUID id, boolean isAvailable) {
        log.info("Updating room availability. Room ID: {}, Available: {}", id, isAvailable);

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));

        room.setIsAvailable(isAvailable);
        Room updatedRoom = roomRepository.save(room);
        return roomMapper.toResponse(updatedRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByRoomNumber(String roomNumber) {
        return roomRepository.existsByRoomNumber(roomNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Room getRoomEntityById(UUID id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
    }

    private void syncAmenities(Room room, List<UUID> amenityIds) {
        roomAmenityRepository.deleteByRoomRoomId(room.getRoomId());
        room.getRoomAmenities().clear();

        if (amenityIds == null || amenityIds.isEmpty()) {
            return;
        }

        List<Amenity> amenities = amenityRepository.findAllById(amenityIds);
        if (amenities.size() != amenityIds.size()) {
            throw new RuntimeException("One or more amenities not found");
        }

        for (Amenity amenity : amenities) {
            RoomAmenity ra = new RoomAmenity();

            RoomAmenityId pk = new RoomAmenityId();
            pk.setRoomId(room.getRoomId());
            pk.setAmenityId(amenity.getAmenityId());

            ra.setId(pk);
            ra.setRoom(room);
            ra.setAmenity(amenity);

            room.getRoomAmenities().add(ra);
        }

        roomAmenityRepository.saveAll(room.getRoomAmenities());
    }


}