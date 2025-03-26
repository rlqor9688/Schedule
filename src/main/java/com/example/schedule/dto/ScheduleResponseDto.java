package com.example.schedule.dto;

import com.example.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    /**
     * ResponseDto는 Client에 전달하는 데이터임.
     * Entity에서 password를 제외한 데이터를 전달하므로, 속성값으로 password는 갖지 않는다.
     */
    private Long id;
    private String task;
    private String writer;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    /**
     *
     * @param schedule RequestDto가 DB를 거쳐 받아온 Entity(Schedule)를 ResponseDto의 입력으로 넣어줌.
     *                 ResponseDto는 이 Entity의 정보를 ResponseDto 형태로 반환함.
     *                 반환 시 비밀번호는 제외.
     */
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.task = schedule.getTask();
        this.writer = schedule.getWriter();
        this.createdDate = schedule.getCreatedDate();
        this.modifiedDate = schedule.getModifiedDate();
    }

}
