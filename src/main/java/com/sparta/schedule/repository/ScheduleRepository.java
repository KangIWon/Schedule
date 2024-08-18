package com.sparta.schedule.repository;

import com.sparta.schedule.entity.Schedule;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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
