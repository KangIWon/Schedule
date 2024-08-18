package com.sparta.schedule.service;

import com.sparta.schedule.Dto.ScheduleRequestDto;
import com.sparta.schedule.Dto.ScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository scheduleRepository;

    @Override
    public Schedule createSchedule(ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = Schedule.builder()
                .task(scheduleRequestDto.getTask())
                .name(scheduleRequestDto.getName())
                .password(scheduleRequestDto.getPassword())
                .build();
        return scheduleRepository.createSchedule(schedule);
    }

    @Override
    public ScheduleResponseDto getSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id);
        return ScheduleResponseDto.from(schedule);
    }

    @Override
    public List<Schedule> getScheduleByNameAndModDate(String name, LocalDate modDate) {
        return scheduleRepository.findByNameAndModDate(name, modDate);
    }

    @Override
    public void deleteScheduleById(Long id) {
        scheduleRepository.deleteById(id);

    }

    @Override
    public Schedule updateScheduleByTaskAndName(Long id, ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = scheduleRepository.findById(id);
        if (!schedule.getPassword().equals(scheduleRequestDto.getPassword())) {
            throw new NoSuchElementException("Password does not match");
        }
        Schedule update = Schedule.builder()
                .id(id)
                .task(scheduleRequestDto.getTask())
                .name(scheduleRequestDto.getName())
                .password(scheduleRequestDto.getPassword())
                .modDate(Timestamp.valueOf(schedule.getModDate()).toLocalDateTime())
                .build();
        return scheduleRepository.update(update);
    }
}
