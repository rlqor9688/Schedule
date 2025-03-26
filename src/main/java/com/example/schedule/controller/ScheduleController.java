package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.service.ScheduleService;
import com.example.schedule.service.ScheduleServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    /**
     * 속성으로 ScheduleService를 가지는 클래스
     */
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 
     * @param scheduleRequestDto : 사용자가 입력한 Body. 이 값을 scheduleService의 saveSchedule 메서드 입력값으로 넣어줌
     * @return ResponseEntity의 입력은 ScheduleResponseDto와 HttpStatus
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {

        return new ResponseEntity<>(scheduleService.saveSchedule(scheduleRequestDto), HttpStatus.OK);
    }


    /**
     * 
     * @param id 사용자 입력값. 수정하고 싶은 글의 고유번호를 사용자가 입력
     * @return 고유 번호에 해당하는 스케줄 출력
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {

        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    };

    /**
     * 
     * @param modifiedDate 사용자 입력값으로 수정일을 받음. 수정일 기준 조회. 필수가 아니므로 (required=false)
     * @param writer 사용자 입력값으로 작성자를 받음. 작성자 기준 조회. 필수가 아니므로 (required=false)
     * @return ResponseEntity의 입력은 List<ScheduleResponseDto>임. 여러 개가 출력될 수 있으므로.
     */
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedule(@RequestParam(required = false) String modifiedDate, @RequestParam(required = false) String writer) {
        return new ResponseEntity<>(scheduleService.findAllSchedules(modifiedDate, writer), HttpStatus.OK);
    }

    /**
     * 
     * @param id 사용자 입력값. 수정하고 싶은 글의 고유번호를 사용자가 입력
     * @param scheduleRequestDto 수정하고 싶은 내용(일정, 작성자)과 비밀번호를 받아와 scheduleService.updateSchedule 메서드에 넘겨줌. 
     * @return ResponseEntity의 입력값은 ScheduleResponseDto. 수정된 스케줄이 반환됨
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, scheduleRequestDto.getTask(), scheduleRequestDto.getWriter(), scheduleRequestDto.getPassword()), HttpStatus.OK);
    }

    /**
     * 
     * @param id 사용자 입력값. 삭제하고 싶은 글의 고유번호를 사용자가 입력
     * @param scheduleRequestDto 비밀번호를 입력 받아 shcduleService.deleteSchedule 메서드로 넘겨줌
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        scheduleService.deleteSchedule(id, scheduleRequestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
