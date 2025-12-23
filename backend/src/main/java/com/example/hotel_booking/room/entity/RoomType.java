package com.example.hotel_booking.room.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "room_types")
@Data
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "type_id")
    private UUID typeId;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "max_guests")
    private Integer maxGuests;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}