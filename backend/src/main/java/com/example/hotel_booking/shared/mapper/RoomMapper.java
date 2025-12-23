package com.example.hotel_booking.shared.mapper;

import com.example.hotel_booking.room.dto.request.RoomRequest;
import com.example.hotel_booking.room.dto.request.RoomTypeRequest;
import com.example.hotel_booking.room.dto.response.AmenityResponse;
import com.example.hotel_booking.room.dto.response.RoomResponse;
import com.example.hotel_booking.room.dto.response.RoomTypeResponse;
import com.example.hotel_booking.room.entity.Room;
import com.example.hotel_booking.room.entity.RoomAmenity;
import com.example.hotel_booking.room.entity.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomType toEntity(RoomTypeRequest request);
    RoomTypeResponse toResponse(RoomType roomType);
    List<RoomTypeResponse> toRoomTypeResponseList(List<RoomType> roomTypes);

    @Mapping(target = "roomType", source = "roomType", qualifiedByName = "roomTypeToResponse")
    @Mapping(target = "amenities", source = "roomAmenities", qualifiedByName = "roomAmenitiesToAmenityResponses")
    RoomResponse toResponse(Room room);

    List<RoomResponse> toResponseList(List<Room> rooms);

    @Mapping(target = "roomNumber", source = "roomNumber")
    @Mapping(target = "floor", source = "floor")
    @Mapping(target = "isAvailable", source = "isAvailable")
    @Mapping(target = "roomType", ignore = true)
    @Mapping(target = "roomAmenities", ignore = true)
    @Mapping(target = "roomId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Room toEntity(RoomRequest request);

    @Named("roomAmenitiesToAmenityResponses")
    default List<AmenityResponse> roomAmenitiesToAmenityResponses(Set<RoomAmenity> roomAmenities) {
        if (roomAmenities == null) return List.of();

        return roomAmenities.stream()
                .map(RoomAmenity::getAmenity)
                .map(a -> {
                    AmenityResponse r = new AmenityResponse();
                    r.setAmenityId(a.getAmenityId());
                    r.setAmenityName(a.getAmenityName());
                    r.setDescription(a.getDescription());
                    r.setCreatedAt(a.getCreatedAt());
                    return r;
                })
                .toList();
    }

    @Named("roomTypeToResponse")
    default RoomTypeResponse roomTypeToResponse(RoomType roomType) {
        if (roomType == null) {
            return null;
        }
        RoomTypeResponse response = new RoomTypeResponse();
        response.setTypeId(roomType.getTypeId());
        response.setTypeName(roomType.getTypeName());
        response.setBasePrice(roomType.getBasePrice());
        response.setMaxGuests(roomType.getMaxGuests());
        response.setDescription(roomType.getDescription());
        response.setCreatedAt(roomType.getCreatedAt());
        return response;
    }
}