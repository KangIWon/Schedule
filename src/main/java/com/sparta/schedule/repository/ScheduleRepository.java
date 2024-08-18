package com.sparta.schedule.repository;

import com.sparta.schedule.entity.Schedule;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.plaf.basic.BasicTreeUI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Repository
public class ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1단계 일정 작성
    public Schedule createSchedule(Schedule schedule) {
        String sql = "INSERT INTO schedule(task, name, password, regDate, modDate) VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime currentTime = LocalDateTime.now();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1,schedule.getTask());
            ps.setString(2,schedule.getName());
            ps.setString(3,schedule.getPassword());
            ps.setObject(4, Timestamp.valueOf(currentTime));
            ps.setObject(5,Timestamp.valueOf(currentTime));
            return ps;
        },keyHolder);
        Long newId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return Schedule.builder()
                .id(newId)
                .task(schedule.getTask())
                .name(schedule.getName())
                .password(schedule.getPassword())
                .regDate(currentTime)
                .modDate(currentTime)
                .build();
    }
    public Schedule findById(Long id) {
        String sql = "SELECT * FROM schedule WHERE id=?";
        try {
            return jdbcTemplate.queryForObject(sql, new ScheduleRowMapper(), id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("일정이 없습니다.");
        }
    }

    public List<Schedule> findByNameAndModDate(String name, LocalDate modDate) {
        StringBuilder sql = new StringBuilder("SELECT * FROM schedule WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (name != null) {
            sql.append(" AND name = ?");
            params.add(name);
        }
        if (modDate != null) {
            LocalDateTime startOfDay = modDate.atStartOfDay();
            sql.append(" AND modDate >= ? AND modDate < ?");
            params.add(Timestamp.valueOf(startOfDay));
            params.add(Timestamp.valueOf(startOfDay.plusDays(1)));
        }
        sql.append("ORDER BY modDate DESC");

        return jdbcTemplate.query(sql.toString(),new ScheduleRowMapper(),params.toArray());
    }

    public Schedule update(Schedule schedule) {
        String sql = "UPDATE schedule SET task = ?, name = ?, modDate = ? WHERE id = ?";
        LocalDateTime currentTime = LocalDateTime.now();
        // Schedule의 modDate를 업데이트하고 저장
        jdbcTemplate.update(sql, schedule.getTask(), schedule.getName(), currentTime, schedule.getId());
        // 업데이트 후, 변경된 schedule 객체를 변환
        return Schedule.builder()
                .id(schedule.getId())
                .task(schedule.getTask())
                .name(schedule.getName())
                .regDate(currentTime)
                .modDate(currentTime)
                .build();
    }

    // 1. entity 자성
    // 2. Dto 작성
    // 3. Repository 작성
    // 4. Service 작성
    // 5. Controller 작성
    // 6. postman 테스트

    public int deleteById(Long id) {
        String sql = "DELETE FROM schedule WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Schedule> findAll() {
        String sql = "SELECT * FROM schedule";
        try {
            return jdbcTemplate.query(sql, new ScheduleRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("");
        }
    }

    private static class ScheduleRowMapper implements RowMapper<Schedule> {
        @Override
        public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Schedule.builder()
                    .id(rs.getLong("id"))
                    .task(rs.getString("task"))
                    .name(rs.getString("name"))
                    .password(rs.getString("password"))
                    .regDate(rs.getTimestamp("regDate").toLocalDateTime())
                    .modDate(rs.getTimestamp("modDate").toLocalDateTime())
                    .build();
        }
    }

}
