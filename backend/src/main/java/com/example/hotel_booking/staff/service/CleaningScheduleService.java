package com.example.hotel_booking.staff.service;

import com.example.hotel_booking.staff.dto.request.CleaningScheduleRequest;
import com.example.hotel_booking.staff.dto.response.CleaningScheduleResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CleaningScheduleService {
    List<CleaningScheduleResponse> getAll();
    CleaningScheduleResponse getById(UUID id);
    List<CleaningScheduleResponse> getByDate(LocalDate date);
    CleaningScheduleResponse create(CleaningScheduleRequest request);
    CleaningScheduleResponse update(UUID id, CleaningScheduleRequest request);
    void delete(UUID id);
}
