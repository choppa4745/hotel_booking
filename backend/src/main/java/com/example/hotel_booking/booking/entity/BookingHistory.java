package com.example.hotel_booking.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "booking_history")
@Data
public class BookingHistory {

    @Id
    @UuidGenerator
    @Column(name = "history_id", updatable = false, nullable = false)
    private UUID historyId;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @Column(name = "old_status")
    private String oldStatus;

    @Column(name = "new_status")
    private String newStatus;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "change_reason")
    private String changeReason;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}
