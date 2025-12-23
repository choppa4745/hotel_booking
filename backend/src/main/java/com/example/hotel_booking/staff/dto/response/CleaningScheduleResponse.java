package com.example.hotel_booking.staff.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class CleaningScheduleResponse {
    private UUID scheduleId;
    private UUID roomId;
    private String roomNumber;
    private UUID employeeId;
    private String employeeName;
    private LocalDate cleaningDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String notes;
    private LocalDateTime createdAt;
}
