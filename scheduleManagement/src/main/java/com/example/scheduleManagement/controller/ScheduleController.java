package com.example.scheduleManagement.controller;

import com.example.scheduleManagement.dto.ScheduleDeleteRequestDto;
import com.example.scheduleManagement.dto.ScheduleRequestDto;
import com.example.scheduleManagement.dto.ScheduleResponseDto;
import com.example.scheduleManagement.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    private ScheduleController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto){
        return new ResponseEntity<>(scheduleService.saveSchedule(scheduleRequestDto), HttpStatus.CREATED);
    }

    @GetMapping()
    public List<ScheduleResponseDto> findAllSchedules(@RequestParam(required = false) String name,
                                                      @RequestParam(required = false) LocalDate modifyDate){
        return scheduleService.findAllSchedules(name, modifyDate);
    }

    @GetMapping("/{scheId}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long scheId){
        return new ResponseEntity<>(scheduleService.findScheduleById(scheId), HttpStatus.OK);
    }

    @PutMapping("/{scheId}")
    public ResponseEntity<ScheduleResponseDto> updateScheduleById(
            @PathVariable Long scheId,
            @RequestBody ScheduleRequestDto scheduleRequestDto
    ){
        return new ResponseEntity<>(scheduleService.updateSchedule(scheId, scheduleRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{scheId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long scheId,
            @RequestBody ScheduleDeleteRequestDto scheduleDeleteRequestDto
    ) {
        scheduleService.deleteSchedule(scheId, scheduleDeleteRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
