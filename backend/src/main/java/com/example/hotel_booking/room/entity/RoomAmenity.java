package com.example.hotel_booking.room.entity;

import jakarta.persistence.*;
import lombok.Data;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "room_amenities")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"room", "amenity"})
public class RoomAmenity {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private RoomAmenityId id = new RoomAmenityId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId")
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("amenityId")
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;
}
