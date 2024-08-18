package com.sparta.schedule.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.jfr.Timestamp;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter // 각 필드 값을 조회 할 수 있는 getter 값을 자동으로 생성 해준다!
@NoArgsConstructor // 기본 생성자 자동 생성
@ToString // 해당 class에 선언된 필드들을 모두 출력할 수 있는 toString메소드를 자동으로 생성할 수 있도록 만들어준다.
public class Schedule {
    //"id":"고유식별자 자동 생성 관리"
    // schedule 테이블의 고유 식별자 선언
    private Long id;
    // 할일 이름
    private String task;
//    // 날짜
//    private String date;
//    // 시간
//    private String time;
    // 담당자명
    private String name;
    // 비밀번호
    @JsonIgnore
    private String password;
    // 작성일
    @Timestamp
    private LocalDateTime regDate;
    // 수정일
    @Timestamp
    private LocalDateTime modDate;

    @Builder
    public Schedule(Long id, String task, String name, String password, LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.task = task;
        this.name = name;
        this.password = password;
        this.regDate = regDate;
        this.modDate = modDate;
    }
}
