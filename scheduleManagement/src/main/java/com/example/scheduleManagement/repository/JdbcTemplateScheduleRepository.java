package com.example.scheduleManagement.repository;

import com.example.scheduleManagement.dto.ScheduleResponseDto;
import com.example.scheduleManagement.entity.Schedule;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("scheId");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", schedule.getName());
        parameters.put("passwd", schedule.getPasswd());
        parameters.put("work", schedule.getWork());
        parameters.put("createDate", schedule.getCreateDate());
        parameters.put("modifyDate", schedule.getModifyDate());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new ScheduleResponseDto(key.longValue(), schedule.getName(), schedule.getWork(), schedule.getCreateDate(), schedule.getModifyDate());
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String name, LocalDate modifyDate) {
        String sql = "SELECT * FROM schedule WHERE (DATE (modifyDate) = ? OR ? IS NULL) AND (name = ? OR ? IS NULL) ORDER BY modifyDate DESC";

        // 파라미터 설정
        Object[] params = {
                modifyDate != null ? modifyDate : null,  // modifyDate
                modifyDate != null ? modifyDate : null,  // modifyDate (조건에 맞게 값 설정)
                name != null ? name : null, // name
                name != null ? name : null // name (조건에 맞게 값 설정)
        };
        return jdbcTemplate.query(sql, params, scheduleRowMapper());
    }

    @Override
    public Optional<Schedule> findScheduleById(Long scheId) {
        List<Schedule> result = jdbcTemplate.query("select * from schedule where scheId = ?", scheduleRowMapperV2(), scheId);

        return result.stream().findAny();
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long scheId) {
        List<Schedule> result = jdbcTemplate.query("select * from schedule where scheid = ?", scheduleRowMapperV2(), scheId);

        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist scheId = " + scheId));
    }

    @Override
    public int updateSchedule(Long scheId, String name, String work, LocalDateTime modifyDate) {

        return jdbcTemplate.update("update schedule set name = ?, work = ?, modifyDate = ? where scheId = ?", name, work, modifyDate, scheId);
    }

    @Override
    public int deleteSchedule(Long scheId) {
        return jdbcTemplate.update("delete from schedule where scheId = ?", scheId);
    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper() {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("scheId"),
                        rs.getString("name"),
                        rs.getString("work"),
                        rs.getObject("createDate", LocalDateTime.class),
                        rs.getObject("modifyDate", LocalDateTime.class)
                );
            }

        };
    }

    private RowMapper<Schedule> scheduleRowMapperV2() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("scheId"),
                        rs.getString("name"),
                        rs.getString("passwd"),
                        rs.getString("work"),
                        rs.getObject("createDate", LocalDateTime.class),
                        rs.getObject("modifyDate", LocalDateTime.class)
                );
            }

        };
    }

}