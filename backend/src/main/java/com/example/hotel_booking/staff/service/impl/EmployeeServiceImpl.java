package com.example.hotel_booking.staff.service.impl;

import com.example.hotel_booking.shared.exception.ResourceNotFoundException;
import com.example.hotel_booking.staff.dto.request.EmployeeRequest;
import com.example.hotel_booking.staff.dto.response.EmployeeResponse;
import com.example.hotel_booking.staff.entity.Employee;
import com.example.hotel_booking.staff.repository.EmployeeRepository;
import com.example.hotel_booking.staff.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAll() {
        return employeeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getById(UUID id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return toResponse(e);
    }

    @Override
    public EmployeeResponse create(EmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Employee with email " + request.getEmail() + " already exists");
        }
        Employee e = new Employee();
        apply(e, request);
        return toResponse(employeeRepository.save(e));
    }

    @Override
    public EmployeeResponse update(UUID id, EmployeeRequest request) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        if (!e.getEmail().equals(request.getEmail()) && employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Employee with email " + request.getEmail() + " already exists");
        }

        apply(e, request);
        return toResponse(employeeRepository.save(e));
    }

    @Override
    public void delete(UUID id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee", "id", id);
        }
        employeeRepository.deleteById(id);
    }

    private void apply(Employee e, EmployeeRequest r) {
        e.setFirstName(r.getFirstName());
        e.setLastName(r.getLastName());
        e.setEmail(r.getEmail());
        e.setPhone(r.getPhone());
        e.setPosition(r.getPosition());
        e.setHireDate(r.getHireDate());
        if (r.getIsActive() != null) e.setIsActive(r.getIsActive());
    }

    private EmployeeResponse toResponse(Employee e) {
        EmployeeResponse r = new EmployeeResponse();
        r.setEmployeeId(e.getEmployeeId());
        r.setFirstName(e.getFirstName());
        r.setLastName(e.getLastName());
        r.setEmail(e.getEmail());
        r.setPhone(e.getPhone());
        r.setPosition(e.getPosition());
        r.setHireDate(e.getHireDate());
        r.setIsActive(e.getIsActive());
        r.setCreatedAt(e.getCreatedAt());
        return r;
    }
}
