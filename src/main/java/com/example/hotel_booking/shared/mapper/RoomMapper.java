package com.example.hotel_booking.shared.mapper;

import com.example.hotel_booking.room.dto.request.RoomRequest;
import com.example.hotel_booking.room.dto.request.RoomTypeRequest;
import com.example.hotel_booking.room.dto.response.RoomResponse;
import com.example.hotel_booking.room.dto.response.RoomTypeResponse;
import com.example.hotel_booking.room.entity.Room;
import com.example.hotel_booking.room.entity.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    // RoomType mappings
    RoomType toEntity(RoomTypeRequest request);
    RoomTypeResponse toResponse(RoomType roomType);
    List<RoomTypeResponse> toRoomTypeResponseList(List<RoomType> roomTypes);

    // Room mappings
    @Mapping(target = "roomType", source = "roomType", qualifiedByName = "roomTypeToResponse")
    RoomResponse toResponse(Room room);

    List<RoomResponse> toResponseList(List<Room> rooms);

    @Mapping(target = "roomId", ignore = true)
    @Mapping(target = "roomType", ignore = true)
    @Mapping(target = "isAvailable", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Room toEntity(RoomRequest request);

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