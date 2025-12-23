package com.example.hotel_booking.staff.repository;

import com.example.hotel_booking.staff.entity.CleaningSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CleaningScheduleRepository extends JpaRepository<CleaningSchedule, UUID> {
    List<CleaningSchedule> findByCleaningDate(LocalDate cleaningDate);
    List<CleaningSchedule> findByRoomRoomId(UUID roomId);
    List<CleaningSchedule> findByEmployeeEmployeeId(UUID employeeId);
}
