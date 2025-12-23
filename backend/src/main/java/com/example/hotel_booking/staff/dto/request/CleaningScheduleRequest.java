package com.example.hotel_booking.staff.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class CleaningScheduleRequest {
    @NotNull private UUID roomId;
    @NotNull private UUID employeeId;
    @NotNull private LocalDate cleaningDate;
    @NotNull private LocalTime startTime;
    private LocalTime endTime;
    private String notes;
}
