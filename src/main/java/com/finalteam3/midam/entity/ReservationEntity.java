package com.finalteam3.midam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "reserve")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String userIdx;


    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String endTime;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String phoneNum;

    private String content;

    @Column(length = 500)
    private String memo;

    //    LocalDate와 비교하기 위한 날짜형식 설정
    public LocalDate getLocalDate() {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // LocalDateTime으로 변환하는 메서드
    // 날짜와 시간을 LocalDateTime으로 반환하는 메서드
    public LocalDateTime getLocalDateTime() {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalTime localTime = LocalTime.parse(startTime); // "00:00:00" 형식으로 저장된 startTime을 파싱
        return LocalDateTime.of(localDate, localTime);
    }
}
