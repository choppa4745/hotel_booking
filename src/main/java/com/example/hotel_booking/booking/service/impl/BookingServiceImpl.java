package com.example.hotel_booking.booking.service.impl;


import com.example.hotel_booking.booking.dto.request.BookingRequest;
import com.example.hotel_booking.booking.dto.response.BookingResponse;
import com.example.hotel_booking.booking.entity.Booking;
import com.example.hotel_booking.booking.entity.BookingGuest;
import com.example.hotel_booking.booking.repository.BookingGuestRepository;
import com.example.hotel_booking.booking.repository.BookingRepository;
import com.example.hotel_booking.booking.service.BookingService;
import com.example.hotel_booking.room.entity.Room;
import com.example.hotel_booking.room.service.RoomService;
import com.example.hotel_booking.shared.exception.ResourceNotFoundException;
import com.example.hotel_booking.shared.mapper.BookingMapper;
import com.example.hotel_booking.user.entity.Guest;
import com.example.hotel_booking.user.service.GuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingGuestRepository bookingGuestRepository;
    private final GuestService guestService;
    private final RoomService roomService;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        log.info("Getting all bookings");
        return bookingRepository.findAll()
                .stream()
                .map(booking -> {
                    List<Guest> additionalGuests = getAdditionalGuests(booking.getBookingId());
                    return bookingMapper.toResponse(booking, additionalGuests);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(UUID id) {
        log.info("Getting booking by id: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        List<Guest> additionalGuests = getAdditionalGuests(id);
        return bookingMapper.toResponse(booking, additionalGuests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByGuest(UUID guestId) {
        log.info("Getting bookings for guest: {}", guestId);
        return bookingRepository.findByGuestGuestId(guestId)
                .stream()
                .map(booking -> {
                    List<Guest> additionalGuests = getAdditionalGuests(booking.getBookingId());
                    return bookingMapper.toResponse(booking, additionalGuests);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByRoom(UUID roomId) {
        log.info("Getting bookings for room: {}", roomId);
        return bookingRepository.findByRoomRoomId(roomId)
                .stream()
                .map(booking -> {
                    List<Guest> additionalGuests = getAdditionalGuests(booking.getBookingId());
                    return bookingMapper.toResponse(booking, additionalGuests);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByStatus(String status) {
        log.info("Getting bookings with status: {}", status);
        return bookingRepository.findByStatus(status)
                .stream()
                .map(booking -> {
                    List<Guest> additionalGuests = getAdditionalGuests(booking.getBookingId());
                    return bookingMapper.toResponse(booking, additionalGuests);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getActiveBookings() {
        log.info("Getting active bookings");
        return bookingRepository.findActiveBookings()
                .stream()
                .map(booking -> {
                    List<Guest> additionalGuests = getAdditionalGuests(booking.getBookingId());
                    return bookingMapper.toResponse(booking, additionalGuests);
                })
                .toList();
    }

    @Override
    public BookingResponse createBooking(BookingRequest request) {
        log.info("Creating new booking for guest: {}, room: {}", request.getGuestId(), request.getRoomId());

        Guest guest = guestService.getGuestEntityById(request.getGuestId());
        Room room = roomService.getRoomEntityById(request.getRoomId());

        if (!isRoomAvailable(room.getRoomId(), request.getCheckInDate(), request.getCheckOutDate())) {
            throw new RuntimeException("Room is not available for the selected dates");
        }

        int totalGuests = 1 + (request.getAdditionalGuestIds() != null ? request.getAdditionalGuestIds().size() : 0);
        if (totalGuests > room.getRoomType().getMaxGuests()) {
            throw new RuntimeException("Too many guests for this room type. Maximum: " + room.getRoomType().getMaxGuests());
        }

        Booking booking = bookingMapper.toEntity(request);
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setStatus("CONFIRMED");

        Booking savedBooking = bookingRepository.save(booking);

        List<Guest> additionalGuests = new ArrayList<>();
        if (request.getAdditionalGuestIds() != null && !request.getAdditionalGuestIds().isEmpty()) {
            for (UUID guestId : request.getAdditionalGuestIds()) {
                Guest additionalGuest = guestService.getGuestEntityById(guestId);

                BookingGuest bookingGuest = new BookingGuest();
                bookingGuest.setBooking(savedBooking);
                bookingGuest.setGuest(additionalGuest);
                bookingGuestRepository.save(bookingGuest);

                additionalGuests.add(additionalGuest);
            }
        }

        roomService.updateRoomAvailability(room.getRoomId(), false);

        log.info("Booking created successfully: {}", savedBooking.getBookingId());
        return bookingMapper.toResponse(savedBooking, additionalGuests);
    }

    @Override
    public BookingResponse updateBooking(UUID id, BookingRequest request) {
        log.info("Updating booking: {}", id);

        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        existingBooking.setCheckInDate(request.getCheckInDate());
        existingBooking.setCheckOutDate(request.getCheckOutDate());

        Booking updatedBooking = bookingRepository.save(existingBooking);
        List<Guest> additionalGuests = getAdditionalGuests(id);
        return bookingMapper.toResponse(updatedBooking, additionalGuests);
    }

    @Override
    public void cancelBooking(UUID id) {
        log.info("Cancelling booking: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        if (!"PENDING".equals(booking.getStatus()) && !"CONFIRMED".equals(booking.getStatus())) {
            throw new RuntimeException("Cannot cancel booking with status: " + booking.getStatus());
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        roomService.updateRoomAvailability(booking.getRoom().getRoomId(), true);
        log.info("Booking cancelled: {}", id);
    }

    @Override
    public BookingResponse confirmBooking(UUID id) {
        log.info("Confirming booking: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        if (!"PENDING".equals(booking.getStatus())) {
            throw new RuntimeException("Cannot confirm booking with status: " + booking.getStatus());
        }

        booking.setStatus("CONFIRMED");
        Booking confirmedBooking = bookingRepository.save(booking);
        List<Guest> additionalGuests = getAdditionalGuests(id);
        return bookingMapper.toResponse(confirmedBooking, additionalGuests);
    }

    @Override
    public BookingResponse checkInBooking(UUID id, UUID employeeId) {
        log.info("Check-in booking: {}, employee: {}", id, employeeId);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw new RuntimeException("Cannot check-in booking with status: " + booking.getStatus());
        }

        booking.setStatus("CHECKED_IN");
        Booking checkedInBooking = bookingRepository.save(booking);
        List<Guest> additionalGuests = getAdditionalGuests(id);
        return bookingMapper.toResponse(checkedInBooking, additionalGuests);
    }

    @Override
    public BookingResponse checkOutBooking(UUID id, UUID employeeId) {
        log.info("Check-out booking: {}, employee: {}", id, employeeId);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        if (!"CHECKED_IN".equals(booking.getStatus())) {
            throw new RuntimeException("Cannot check-out booking with status: " + booking.getStatus());
        }

        booking.setStatus("CHECKED_OUT");
        Booking checkedOutBooking = bookingRepository.save(booking);

        roomService.updateRoomAvailability(booking.getRoom().getRoomId(), true);

        List<Guest> additionalGuests = getAdditionalGuests(id);
        return bookingMapper.toResponse(checkedOutBooking, additionalGuests);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRoomAvailable(UUID roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(roomId, checkInDate, checkOutDate);
        return conflictingBookings.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> findConflictingBookings(UUID roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> conflicts = bookingRepository.findConflictingBookings(roomId, checkInDate, checkOutDate);
        return conflicts.stream()
                .map(booking -> {
                    List<Guest> additionalGuests = getAdditionalGuests(booking.getBookingId());
                    return bookingMapper.toResponse(booking, additionalGuests);
                })
                .toList();
    }

    private List<Guest> getAdditionalGuests(UUID bookingId) {
        return bookingGuestRepository.findByBookingBookingId(bookingId)
                .stream()
                .map(BookingGuest::getGuest)
                .toList();
    }
}