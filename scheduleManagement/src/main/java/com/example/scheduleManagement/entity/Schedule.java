package com.example.scheduleManagement.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Schedule {
    private Long scheId;

    private String name;
    private String passwd;
    private String work;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public Schedule(String name, String passwd, String work, LocalDateTime createDate, LocalDateTime modifyDate){
        this.name = name;
        this.passwd = passwd;
        this.work = work;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

}

