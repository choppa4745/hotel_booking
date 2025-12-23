package com.example.hotel_booking.room.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "amenities")
@Data
public class Amenity {
    @Id
    @UuidGenerator
    @Column(name = "amenity_id")
    private UUID amenityId;

    @Column(name = "amenity_name", nullable = false, unique = true)
    private String amenityName;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "amenity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomAmenity> roomAmenities = new HashSet<>();

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
