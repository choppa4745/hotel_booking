package com.example.hotel_booking.shared.mapper;


import com.example.hotel_booking.user.dto.request.GuestRequest;
import com.example.hotel_booking.user.dto.response.GuestResponse;
import com.example.hotel_booking.user.entity.Guest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "guestId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Guest toEntity(GuestRequest request);

    GuestResponse toResponse(Guest guest);

    List<GuestResponse> toResponseList(List<Guest> guests);
}