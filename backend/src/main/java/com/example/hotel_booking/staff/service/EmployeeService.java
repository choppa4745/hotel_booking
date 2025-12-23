package com.example.hotel_booking.staff.service;

import com.example.hotel_booking.staff.dto.request.EmployeeRequest;
import com.example.hotel_booking.staff.dto.response.EmployeeResponse;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    List<EmployeeResponse> getAll();
    EmployeeResponse getById(UUID id);
    EmployeeResponse create(EmployeeRequest request);
    EmployeeResponse update(UUID id, EmployeeRequest request);
    void delete(UUID id);
}
