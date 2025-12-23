package com.example.hotel_booking.booking.entity;


import com.example.hotel_booking.user.entity.Guest;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "booking_guests")
@Data
public class BookingGuest {
    @Id
    @UuidGenerator
    @Column(name = "booking_guest_id", updatable = false, nullable = false)
    private UUID bookingGuestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}