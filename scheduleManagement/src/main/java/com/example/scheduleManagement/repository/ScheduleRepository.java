package com.example.scheduleManagement.repository;

import com.example.scheduleManagement.dto.ScheduleResponseDto;
import com.example.scheduleManagement.entity.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    ScheduleResponseDto saveSchedule(Schedule schedule);

    List<ScheduleResponseDto> findAllSchedules(String name, LocalDate modifyDate);

    Optional<Schedule> findScheduleById(Long scheId);

    Schedule findScheduleByIdOrElseThrow(Long scheId);

    int updateSchedule(Long scheId, String name, String work, LocalDateTime modifyDate);

    int deleteSchedule(Long scheId);
}
