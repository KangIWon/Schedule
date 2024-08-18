package com.sparta.schedule.service;

import com.sparta.schedule.Dto.ScheduleRequestDto;
import com.sparta.schedule.Dto.ScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;

public interface ScheduleService {
    Schedule createSchedule(ScheduleRequestDto scheduleRequestDto);

    ScheduleResponseDto getSchedule(Long id);

}
