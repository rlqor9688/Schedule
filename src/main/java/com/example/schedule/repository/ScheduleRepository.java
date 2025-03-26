package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository {
    // ScheduleServiceImp 클래스에서 변환한 Entity(schedule)을 repository(jdbc)에 저장하는 메서드
    ScheduleResponseDto saveSchedule(Schedule schedule);

    // 입력받은 작성일, 수정일 정보를 바탕으로 repository에서 데이터들을 조회해 List<ScheculeResponseDto> 형식으로 반환하는 메서드
    List<ScheduleResponseDto> findAllSchedules(String modifiedDate, String writer);


    // 입력받은 id와 매칭되는 데이터는 repository에서 조회해 Schedule 타입으로 반환하는 메서드.
    // 이 Schedule 타입을 받아 ScheduleService에서 ResponseDto에 입력으로 넣어 반환해줌
    // 매칭되는 ID가 없으면 HttpStatus.NOT_FOUND 출력
    Schedule findScheduleByIdOrElseThrow(Long id);

    // 입력받은 id, 할일, 작성자, 비밀번호를 바탕으로 스케줄을 업데이트하는 메서드.
    // 업데이트한 스케줄의 개수를 int로 반환해줌
    int updateSchedule(Long id, String task, String writer, String password);

    // 입력받은 id, 비밀번호를 바탕으로 스케줄을 삭제하는 메서드
    // 삭제한 스케줄의 개수를 int로 반환해줌
    int deleteSchedule(Long id, String password);
}


