package com.finalteam3.midam.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "treatSale")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TreatSaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(name = "user_name", nullable = true)
    private String userName;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer userIdx;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String paymentDay;

    @Column(nullable = false)
    private String paymentTime;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String payment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime  createDate =LocalDateTime.now();

    @Column(length = 500)
    private String memo;
}
