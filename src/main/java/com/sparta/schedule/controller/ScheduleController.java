package com.sparta.schedule.controller;

import com.sparta.schedule.Dto.ScheduleRequestDto;
import com.sparta.schedule.Dto.ScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.service.ScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController {
    private final ScheduleServiceImpl scheduleServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        log.info("Regiser schedule request: {}", scheduleRequestDto);
        return ResponseEntity.ok(scheduleServiceImpl.createSchedule(scheduleRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getById(@PathVariable Long id) {
        if (scheduleServiceImpl.getSchedule(id)==null) {
            return ResponseEntity.notFound().build();
        }
        log.info("Get by Id: {}", id);
        return ResponseEntity.ok(scheduleServiceImpl.getSchedule(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Schedule>> getByNameAndModDate(@RequestParam(required = false) String name, @RequestParam(required = false)LocalDate modDate) {
        List<Schedule> schedules = scheduleServiceImpl.getScheduleByNameAndModDate(name, modDate);
        log.info("Get list by name: {}, modDate: {}",name,modDate);
        if (schedules.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(schedules);
    }
}
