package com.finalteam3.midam.repository;

import com.finalteam3.midam.entity.TreatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TreatRepository extends JpaRepository<TreatEntity, Integer> {

    //    시술리스트 불러오기
    List<TreatEntity> findAllByOrderByTreatAmount();

    //    시술 편집 시 데이터 불러오기
    TreatEntity findByTreatIdx(int treatIdx);

    //    시술내역 편집 처리하기
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update TreatEntity e SET e.treatName = :treatName , e.treatTimeHour = :treatTimeHour, e.treatTimeMinute = :treatTimeMinute, e.treatAmount = :treatAmount, e.gender = :gender WHERE e.treatIdx= :treatIdx")
    void updateTreatList(int treatIdx, String treatName, String treatTimeHour, String treatTimeMinute, int treatAmount, String gender);

    List<TreatEntity> findAllByOrderByGenderAscTreatAmountAsc();

    List<TreatEntity> findByGender(String gender);
}
