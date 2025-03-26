package com.example.schedule.dto;

import com.example.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
/**
 * 사용자로부터 입력 받는 값은 할 일, 작성자, 비밀번호임
 */
public class ScheduleRequestDto {
    private String task;
    private String writer;
    private String password;
}