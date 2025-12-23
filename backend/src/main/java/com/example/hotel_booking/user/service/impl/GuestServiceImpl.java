package com.example.hotel_booking.user.service.impl;


import com.example.hotel_booking.shared.exception.ResourceNotFoundException;
import com.example.hotel_booking.shared.mapper.UserMapper;
import com.example.hotel_booking.user.dto.request.GuestRequest;
import com.example.hotel_booking.user.dto.response.GuestResponse;
import com.example.hotel_booking.user.entity.Guest;
import com.example.hotel_booking.user.repository.GuestRepository;
import com.example.hotel_booking.user.service.GuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GuestResponse> getAllGuests() {
        log.info("Getting all guests");
        return guestRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GuestResponse getGuestById(UUID id) {
        log.info("Getting guest by id: {}", id);
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));
        return userMapper.toResponse(guest);
    }

    @Override
    @Transactional(readOnly = true)
    public GuestResponse getGuestByEmail(String email) {
        log.info("Getting guest by email: {}", email);
        Guest guest = guestRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "email", email));
        return userMapper.toResponse(guest);
    }

    @Override
    @Transactional(readOnly = true)
    public GuestResponse getGuestByPassportNumber(String passportNumber) {
        log.info("Getting guest by passport number: {}", passportNumber);
        Guest guest = guestRepository.findByPassportNumber(passportNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "passportNumber", passportNumber));
        return userMapper.toResponse(guest);
    }

    @Override
    public GuestResponse createGuest(GuestRequest request) {
        log.info("Creating new guest: {} {}", request.getFirstName(), request.getLastName());

        guestRepository.findByEmail(request.getEmail())
                .ifPresent(g -> {
                    throw new RuntimeException("Guest with email " + request.getEmail() + " already exists");
                });

        if (request.getPassportNumber() != null) {
            guestRepository.findByPassportNumber(request.getPassportNumber())
                    .ifPresent(g -> {
                        throw new RuntimeException("Guest with passport number " + request.getPassportNumber() + " already exists");
                    });
        }

        Guest guest = userMapper.toEntity(request);

         if (guest.getCreatedAt() == null) {
            guest.setCreatedAt(LocalDateTime.now());
        }

        Guest savedGuest = guestRepository.save(guest);
        return userMapper.toResponse(savedGuest);
    }


    @Override
    public GuestResponse updateGuest(UUID id, GuestRequest request) {
        log.info("Updating guest with id: {}", id);

        Guest existingGuest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));

        existingGuest.setFirstName(request.getFirstName());
        existingGuest.setLastName(request.getLastName());
        existingGuest.setPhone(request.getPhone());
        existingGuest.setDateOfBirth(request.getDateOfBirth());
        existingGuest.setEmail(request.getEmail());
        existingGuest.setPassportNumber(request.getPassportNumber());

        Guest updatedGuest = guestRepository.save(existingGuest);
        return userMapper.toResponse(updatedGuest);
    }

    @Override
    public void deleteGuest(UUID id) {
        log.info("Deleting guest with id: {}", id);

        if (!guestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Guest", "id", id);
        }

        guestRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return guestRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPassportNumber(String passportNumber) {
        return guestRepository.findByPassportNumber(passportNumber).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public Guest getGuestEntityById(UUID id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "id", id));
    }
}