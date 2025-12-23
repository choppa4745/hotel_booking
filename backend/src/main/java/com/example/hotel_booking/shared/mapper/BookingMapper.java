package com.example.hotel_booking.shared.mapper;


import com.example.hotel_booking.booking.dto.request.BookingRequest;
import com.example.hotel_booking.booking.dto.response.BookingResponse;
import com.example.hotel_booking.booking.entity.Booking;
import com.example.hotel_booking.room.dto.response.RoomResponse;
import com.example.hotel_booking.room.entity.Room;
import com.example.hotel_booking.user.dto.response.GuestResponse;
import com.example.hotel_booking.user.entity.Guest;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RoomMapper.class})
public interface BookingMapper {

    @Mapping(target = "bookingId", ignore = true)
    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Booking toEntity(BookingRequest request);

    @Mapping(target = "additionalGuests", ignore = true)
    BookingResponse toResponse(Booking booking);

    default BookingResponse toResponse(Booking booking, List<Guest> additionalGuests,
                                       UserMapper userMapper, RoomMapper roomMapper) {
        BookingResponse response = toResponse(booking);

        if (booking.getGuest() != null) {
            response.setGuest(userMapper.toResponse(booking.getGuest()));
        }

        if (booking.getRoom() != null) {
            response.setRoom(roomMapper.toResponse(booking.getRoom()));
        }

        if (additionalGuests != null && !additionalGuests.isEmpty()) {
            response.setAdditionalGuests(userMapper.toResponseList(additionalGuests));
        }

        return response;
    }

    @Named("toGuestResponse")
    default GuestResponse toGuestResponse(Guest guest, @Context UserMapper userMapper) {
        return userMapper.toResponse(guest);
    }

    @Named("toRoomResponse")
    default RoomResponse toRoomResponse(Room room, @Context RoomMapper roomMapper) {
        return roomMapper.toResponse(room);
    }
}