package com.example.hotel_booking.shared.mapper;



import com.example.hotel_booking.room.dto.request.RoomRequest;
import com.example.hotel_booking.room.dto.request.RoomTypeRequest;
import com.example.hotel_booking.room.dto.response.RoomResponse;
import com.example.hotel_booking.room.dto.response.RoomTypeResponse;
import com.example.hotel_booking.room.entity.Room;
import com.example.hotel_booking.room.entity.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomType toEntity(RoomTypeRequest request);
    RoomTypeResponse toResponse(RoomType roomType);

    @Mapping(target = "roomType", source = "roomType")
    Room toEntity(RoomRequest request);

    @Mapping(target = "roomType", source = "roomType")
    RoomResponse toResponse(Room room);
}