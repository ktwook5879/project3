package com.finalteam3.midam.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userIdx;

    @Column (length = 100, nullable = false)
    private String userName;

    @Column (nullable = false)
    private String gender;

    @Column (nullable = false)
    private String userPhone;

    @Column (unique = true ,nullable = false)
    private String userId;

    @Column (nullable = false)
    private String userPwd1;

    @Column (nullable = false)
    private String userPwd2;

    @Column(nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(nullable = false)
    private String adminYn;


    //    adminYn은 모두 기본값 n으로 들어가기 관리자는 한명만 처리하면됨 Y
    @PrePersist
    public void prePersist(){
        if(this.adminYn==null){
            this.adminYn ="N";
        };
    };

    @Column(nullable = true)
    private String userMemo;

}