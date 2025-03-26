package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.mysql.cj.result.Row;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 
     * @param : ScheduleServiceImp 클래스에서 RequestDto를 Schedule 타입으로 변경해주고, 이 데이터를 입력 
     * @return : Jdbc에 저장 후 ScheduleResponseDto 형식으로 변환. 저장 시 id 자동 생성.
     */
    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        // INSERT Query 직접 작성하지 않아도 된다.
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("scheduleList").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", schedule.getTask());
        parameters.put("writer", schedule.getWriter());
        parameters.put("password", schedule.getPassword());
        parameters.put("createdDate", schedule.getCreatedDate());
        parameters.put("modifiedDate", schedule.getModifiedDate());

        // 저장 후 생성된 Key값 Number 타입으로 반환하는 메서드
        // 의문: ScheduleResponseDto는 입력으로 Schedule 형식을 받도록 했는데, 이렇게 값을 쪼개서 넣어줘도 ScheduleResponseDto가 왜 생성이 되는지?
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new ScheduleResponseDto(key.longValue(), schedule.getTask(), schedule.getWriter(), schedule.getCreatedDate(), schedule.getModifiedDate());
    }

    /**
     * 
     * @param modifiedDateInput 수정일 입력값(유저)
     * @param writerInput 작성자 입력값(유저)
     * if문 -> 입력이 하나라도 있는 경우 -> 쿼리 / else(입력이 둘 다 없는 경우) -> 전체 조회
     */
    @Override
    public List<ScheduleResponseDto> findAllSchedules(String modifiedDateInput, String writerInput) {
        if (modifiedDateInput != null || writerInput !=null) {
            return jdbcTemplate.query("select * from scheduleList where DATE_FORMAT(modifiedDate, '%Y-%m-%d') = ? OR writer = ? order by modifiedDate desc", scheduleRowMapper(), modifiedDateInput, writerInput);
        } else {
            return jdbcTemplate.query("select * from scheduleList order by modifiedDate desc", scheduleRowMapper());
        }

    }

    /**
     * 
     * @param : 사용자가 입력한 id에 매칭되는 데이터를 jdbc에서 쿼리로 조회해 반환(Schedule)
     * @return Schedule
     */
    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("select * from scheduleList where id = ?", scheduleRowMapperV2(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists id = " + id));
    }

    /**
     * RowMapper라는 형식이 있나봄. 입력한 데이터와 매칭되는 쿼리를 찾아 반환해주는 듯. 원리와 구조를 제대로 이해할 필요가 있음 
     * @return
     */
    private RowMapper<Schedule> scheduleRowMapperV2() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getString("task"),
                        rs.getString("writer"),
                        rs.getString("password"),
                        rs.getTimestamp("createdDate").toLocalDateTime(),
                        rs.getTimestamp("modifiedDate").toLocalDateTime()
                );
            }
        };
    }

    /**
     * 
     * @param id : 스케줄 고유번호(사용자 입력)
     * @param task : 수정할 할 일(사용자 입력)
     * @param writer : 수정할 작성자명(사용자 입력)
     * @param password : 비밀번호(검증용)
     * @return 수정된 데이터 수. 데이터를 jdbc에 업데이트 해주고, 수정된 데이터 수를 반환해줌.
     * 수정된 데이터를 찾는 로직은 findScheduleByIdOrElseThrow(id)를 이용해 수정된 데이터 확인 가능 
     */
    @Override
    public int updateSchedule(Long id, String task, String writer, String password) {

        return jdbcTemplate.update("update scheduleList set task= ?, writer = ?, modifiedDate =? where id = ? and password = ?", task, writer, LocalDateTime.now(), id, password);
    }

    /**
     * 
     * @param id : 수정할 스케줄 고유번호(사용자 입력)
     * @param password : 비밀번호(검증용)
     * @return 삭제된 데이터 수. 데이터를 jdbc에서 삭제 해주고, 삭제된 데이터 수를 반환해줌.
     */
    @Override
    public int deleteSchedule(Long id, String password) {
        return jdbcTemplate.update("delete from scheduleList where id = ? and password = ?", id, password);
    }

    /**
     * findAllSchedules 메서드에서 쿼리와 일치하는 데이터를 매칭할 때 사용.
     * @return
     */
    private RowMapper<ScheduleResponseDto> scheduleRowMapper() {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("task"),
                        rs.getString("writer"),
                        rs.getTimestamp("createdDate").toLocalDateTime(),
                        rs.getTimestamp("modifiedDate").toLocalDateTime()
                );
            }
        };
    }


}
