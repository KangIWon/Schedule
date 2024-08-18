package com.sparta.schedule.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
// @Getter / @ToString / @RequireArgConstructor
public class ScheduleRequestDto {
    //"id":"고유식별자 자동 생성 관리"
    // schedule 테이블의 고유 식별자 선언
//    private Long id;
    // 할일 이름
    private String task;
    //    // 날짜
//    private String date;
//    // 시간
//    private String time;
    // 담당자명
    private String name;
    // 비밀번호
    private String password;
//    // 작성일
//    private LocalDateTime regDate;
//    // 수정일
//    private LocalDateTime modDate;

    @Builder
    public ScheduleRequestDto(String task, String name, String password) {
        this.task = task;
        this.name = name;
        this.password = password;
    }
}
