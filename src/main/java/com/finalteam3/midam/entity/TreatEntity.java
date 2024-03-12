package com.finalteam3.midam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Treat")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TreatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer treatIdx;

//    시술명
//    시술금액
//    시술시간 hour
//    시술시간 minute

    @Column(nullable = false)
    private String treatName;

    @Column(nullable = false)
    private String treatTimeHour;

    @Column(nullable = false)
    private String treatTimeMinute;

    @Column(nullable = false)
    private Integer treatAmount;

    @Column(nullable = false)
    private String gender;

}
