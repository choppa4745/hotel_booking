package com.example.hotel_booking.staff.service.impl;

import com.example.hotel_booking.room.entity.Room;
import com.example.hotel_booking.room.service.RoomService;
import com.example.hotel_booking.shared.exception.ResourceNotFoundException;
import com.example.hotel_booking.staff.dto.request.CleaningScheduleRequest;
import com.example.hotel_booking.staff.dto.response.CleaningScheduleResponse;
import com.example.hotel_booking.staff.entity.CleaningSchedule;
import com.example.hotel_booking.staff.entity.Employee;
import com.example.hotel_booking.staff.repository.CleaningScheduleRepository;
import com.example.hotel_booking.staff.repository.EmployeeRepository;
import com.example.hotel_booking.staff.service.CleaningScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CleaningScheduleServiceImpl implements CleaningScheduleService {

    private final CleaningScheduleRepository cleaningScheduleRepository;
    private final RoomService roomService;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CleaningScheduleResponse> getAll() {
        return cleaningScheduleRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CleaningScheduleResponse getById(UUID id) {
        CleaningSchedule cs = cleaningScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CleaningSchedule", "id", id));
        return toResponse(cs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CleaningScheduleResponse> getByDate(LocalDate date) {
        return cleaningScheduleRepository.findByCleaningDate(date).stream().map(this::toResponse).toList();
    }

    @Override
    public CleaningScheduleResponse create(CleaningScheduleRequest request) {
        Room room = roomService.getRoomEntityById(request.getRoomId());
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getEmployeeId()));

        CleaningSchedule cs = new CleaningSchedule();
        cs.setRoom(room);
        cs.setEmployee(employee);
        apply(cs, request);

        return toResponse(cleaningScheduleRepository.save(cs));
    }

    @Override
    public CleaningScheduleResponse update(UUID id, CleaningScheduleRequest request) {
        CleaningSchedule cs = cleaningScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CleaningSchedule", "id", id));

        Room room = roomService.getRoomEntityById(request.getRoomId());
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", request.getEmployeeId()));

        cs.setRoom(room);
        cs.setEmployee(employee);
        apply(cs, request);

        return toResponse(cleaningScheduleRepository.save(cs));
    }

    @Override
    public void delete(UUID id) {
        if (!cleaningScheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("CleaningSchedule", "id", id);
        }
        cleaningScheduleRepository.deleteById(id);
    }

    private void apply(CleaningSchedule cs, CleaningScheduleRequest r) {
        cs.setCleaningDate(r.getCleaningDate());
        cs.setStartTime(r.getStartTime());
        cs.setEndTime(r.getEndTime());
        cs.setNotes(r.getNotes());
    }

    private CleaningScheduleResponse toResponse(CleaningSchedule cs) {
        CleaningScheduleResponse r = new CleaningScheduleResponse();
        r.setScheduleId(cs.getScheduleId());
        r.setRoomId(cs.getRoom().getRoomId());
        r.setRoomNumber(cs.getRoom().getRoomNumber());
        r.setEmployeeId(cs.getEmployee().getEmployeeId());
        r.setEmployeeName(cs.getEmployee().getFirstName() + " " + cs.getEmployee().getLastName());
        r.setCleaningDate(cs.getCleaningDate());
        r.setStartTime(cs.getStartTime());
        r.setEndTime(cs.getEndTime());
        r.setNotes(cs.getNotes());
        r.setCreatedAt(cs.getCreatedAt());
        return r;
    }
}
