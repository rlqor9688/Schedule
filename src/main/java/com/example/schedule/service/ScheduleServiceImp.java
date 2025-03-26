package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImp implements ScheduleService{

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImp(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * 
     * @param scheduleRequestDto 사용자 입력값을 받아 schedule로 변환해주고, 이 값을 ScheduleServiceImp에 입력해 DB 저장 후 ResponseDto로 반환
     * @return
     */
    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto) {
        // 요청받은 데이터로 Schedule 객체 생성
        Schedule schedule = new Schedule(scheduleRequestDto.getTask(), scheduleRequestDto.getWriter(), scheduleRequestDto.getPassword());
        return scheduleRepository.saveSchedule(schedule);
    }

    /**
     * 사용자로부터 입력받은 수정일, 작성자 정보를 바탕으로 DB에서 데이터 찾는 메서드 제어
     */
    @Override
    public List<ScheduleResponseDto> findAllSchedules(String modifiedDate, String writer) {
        return scheduleRepository.findAllSchedules(modifiedDate, writer);
    }


    /**
     * 
     * @param id 사용자로부터 입력받는 스케줄 고유번호
     * @return 해당하는 스케줄을 ResponseDto로 변경해 반환
     */
    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);

        return new ScheduleResponseDto(schedule);
    }

    /**
     * 
     * @param id 사용자가 입력한 스케줄 고유번호
     * @param task 사용자가 입력한 수정할 할일
     * @param writer 사용자가 입력한 수정할 작성자명
     * @param password 사용자가 입력한 비밀번호(검증용)
     * @return
     */
    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String task, String writer, String password) {
        // 값이 하나라도 없으면 BAD_REQUEST 출력
        if (task == null || writer == null || password==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The task, writer and password are required values.");
        } 
        
        // DB에 업데이트 후 변경된 데이터의 개수 반환
        int updatedRow = scheduleRepository.updateSchedule(id, task, writer, password);

        // 변경된 데이터의 수가 0개면, 매칭되는 id가 없으므로 NOT_FOUND 메시지 출력
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
        
        // 변경이 성공된 경우, 변경한 스케줄 고유번호(id)에 해당하는 Schedule Entity를 받아 ResponseDto로 변환
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    /**
     * 
     * @param id 사용자가 입력한 스케줄 id
     * @param password 사용자가 입력한 비밀번호(검증용)
     */
    @Override
    public void deleteSchedule(Long id, String password) {
        // 삭제된 데이터의 수를 반환
        int deletedRow = scheduleRepository.deleteSchedule(id, password);
        
        // 삭제된 데이터가 0개면 NOT_FOUND 메시지 출력
        if (deletedRow==0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id or password does not match");
        }
        // 삭제시 반환값 없음
    }
}
