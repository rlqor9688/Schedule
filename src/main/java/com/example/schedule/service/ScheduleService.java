package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.JdbcTemplateScheduleRepository;

import java.util.List;

public interface ScheduleService {
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto);
    public List<ScheduleResponseDto> findAllSchedules(String modifiedDate, String writer);
    public ScheduleResponseDto findScheduleById(Long id);
    public ScheduleResponseDto updateSchedule(Long id, String task, String writer, String password);
    public void deleteSchedule(Long id, String password);
}
