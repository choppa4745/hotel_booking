package com.example.hotel_booking.staff.controller;

import com.example.hotel_booking.staff.dto.request.CleaningScheduleRequest;
import com.example.hotel_booking.staff.dto.response.CleaningScheduleResponse;
import com.example.hotel_booking.staff.service.CleaningScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cleaning")
@RequiredArgsConstructor
public class CleaningScheduleController {

    private final CleaningScheduleService cleaningScheduleService;

    @GetMapping
    public List<CleaningScheduleResponse> getAll() {
        return cleaningScheduleService.getAll();
    }

    @GetMapping("/{id}")
    public CleaningScheduleResponse getById(@PathVariable UUID id) {
        return cleaningScheduleService.getById(id);
    }

    @GetMapping("/date")
    public List<CleaningScheduleResponse> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return cleaningScheduleService.getByDate(date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CleaningScheduleResponse create(@Valid @RequestBody CleaningScheduleRequest request) {
        return cleaningScheduleService.create(request);
    }

    @PutMapping("/{id}")
    public CleaningScheduleResponse update(@PathVariable UUID id, @Valid @RequestBody CleaningScheduleRequest request) {
        return cleaningScheduleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        cleaningScheduleService.delete(id);
    }
}
