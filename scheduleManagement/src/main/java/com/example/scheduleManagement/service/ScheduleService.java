package com.example.scheduleManagement.service;

import com.example.scheduleManagement.dto.ScheduleDeleteRequestDto;
import com.example.scheduleManagement.dto.ScheduleRequestDto;
import com.example.scheduleManagement.dto.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto);

    List<ScheduleResponseDto> findAllSchedules(String name, LocalDate modifyDate);

    ScheduleResponseDto findScheduleById(Long scheid);

    ScheduleResponseDto updateSchedule(Long scheId, ScheduleRequestDto scheduleRequestDto);

    void deleteSchedule(Long scheId, ScheduleDeleteRequestDto scheduleDeleteRequestDto);

}