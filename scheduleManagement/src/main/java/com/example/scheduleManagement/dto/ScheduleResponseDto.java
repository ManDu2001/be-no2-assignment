package com.example.scheduleManagement.dto;

import com.example.scheduleManagement.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long scheId;
    private String name;
    private String work;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public ScheduleResponseDto(Schedule schedule) {
        this.scheId = schedule.getScheId();
        this.name = schedule.getName();
        this.work = schedule.getWork();
        this.createDate = schedule.getCreateDate();
        this.modifyDate = schedule.getModifyDate();
    }
}
