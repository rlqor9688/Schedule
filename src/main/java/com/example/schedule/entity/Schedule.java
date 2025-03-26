package com.example.schedule.entity;

import com.example.schedule.dto.ScheduleRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
/**
 * 스케줄은 번호, 할일, 작성자, 비밀번호, 작성일, 수정일을 데이터 속성(컬럼)으로 가짐
 */
public class Schedule {
    private Long id;
    private String task;
    private String writer;
    private String password;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    /**
     * 최초 생성 시 아래의 세 가지 값(RequestDto에서 넘어옴)을 받아 생성함
     * @param task 할 일
     * @param writer 작성자
     * @param password 비밀번호
     * createdDate는 현재 시간을 기록하고, 생성 시점의 modifiedDate는 createdDate와 동일하게 설정
     */
    public Schedule(String task, String writer, String password) {
        this.task = task;
        this.writer = writer;
        this.password = password;
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = this.createdDate;
    }


    /**
     *
     * @param scheduleRequestDto 사용자 입력으로 할일(task), 작성자(writer), 비밀번호(password)를 입력받아 schedule 내용 업데이트
     *                           반환값이 없고 schedule의 속성만 업데이트 되므로 출력 형식은 void
     */
    public void updateSchedule(ScheduleRequestDto scheduleRequestDto) {
        this.task = scheduleRequestDto.getTask();
        this.writer = scheduleRequestDto.getWriter();
        this.modifiedDate = LocalDateTime.now();
    }

}
