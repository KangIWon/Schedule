package com.sparta.schedule.service;

import com.sparta.schedule.Dto.ScheduleRequestDto;
import com.sparta.schedule.Dto.ScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
