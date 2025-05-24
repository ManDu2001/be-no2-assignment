package com.example.scheduleManagement.service;

import com.example.scheduleManagement.dto.ScheduleDeleteRequestDto;
import com.example.scheduleManagement.dto.ScheduleRequestDto;
import com.example.scheduleManagement.dto.ScheduleResponseDto;
import com.example.scheduleManagement.entity.Schedule;
import com.example.scheduleManagement.exception.InvalidRequestException;
import com.example.scheduleManagement.exception.ResourceNotFoundException;
import com.example.scheduleManagement.exception.UnauthorizedException;
import com.example.scheduleManagement.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto){
        Schedule schedule = new Schedule(scheduleRequestDto.getName(), scheduleRequestDto.getPasswd(), scheduleRequestDto.getWork(),
                LocalDateTime.now(), LocalDateTime.now());

        return scheduleRepository.saveSchedule(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String name, LocalDate modifyDate) {

        return scheduleRepository.findAllSchedules(name, modifyDate);
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long scheId){
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(scheId);

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long scheId, ScheduleRequestDto scheduleRequestDto) {

        Optional<Schedule> scheduleOptional = scheduleRepository.findScheduleById(scheId);

        if (scheduleOptional.isPresent()) {
            Schedule schedule = scheduleOptional.get();
            if (!schedule.getPasswd().equals(scheduleRequestDto.getPasswd())) {
                throw new UnauthorizedException("Wrong password");
            }

            String name = scheduleRequestDto.getName();
            String work = scheduleRequestDto.getWork();

            // 필수값 검증
            if (name == null || work == null) {
                throw new InvalidRequestException("The name and work are required values.");
            }

            LocalDateTime modifyDate = LocalDateTime.now();
            int updatedRow = scheduleRepository.updateSchedule(scheId, name, work, modifyDate);
            if (updatedRow == 0) {
                throw new ResourceNotFoundException("No data has been modified.");
            }

            schedule = scheduleRepository.findScheduleByIdOrElseThrow(scheId);

            return new ScheduleResponseDto(schedule);

        } else {
            throw new ResourceNotFoundException("Does not exist scheId = " + scheId);
        }

    }

    @Override
    public void deleteSchedule(Long scheId, ScheduleDeleteRequestDto scheduleDeleteRequestDto) {
        Optional<Schedule> scheduleOptional = scheduleRepository.findScheduleById(scheId);

        if (scheduleOptional.isPresent()) {
            Schedule schedule = scheduleOptional.get();
            if (!schedule.getPasswd().equals(scheduleDeleteRequestDto.getPasswd())) {
                throw new UnauthorizedException("Wrong password");
            }

            int deletedRow = scheduleRepository.deleteSchedule(scheId);

        } else {
            throw new ResourceNotFoundException("Does not exist scheId = " + scheId);
        }

    }

}