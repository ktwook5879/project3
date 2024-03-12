package com.finalteam3.midam.service;

import com.finalteam3.midam.entity.TreatEntity;

import java.util.List;

public interface TreatService {

    //    시술메뉴 불러오기
    List<TreatEntity> selectTreatList() throws Exception;

    //    시술메뉴 추가하기
    void treatSave(TreatEntity treatEntity) throws Exception;

    //    시술메뉴 수정위해 기존 데이터 불러오기
    TreatEntity findTreatList(int treatIdx);

    //    시술메뉴 수정하기
    void updateTreatList(int treatIdx, String treatName, String treatTimeHour, String treatTimeMinute, int treatAmount, String gender);

    //    시술메뉴 삭제하기
    void deleteTreatList(int treatIdx);

    // 시술메뉴 정렬하기
    List<TreatEntity> findAllSortedByGenderAndTreatAmount() throws Exception;

    List<TreatEntity> getTreatsByGender(String gender) throws Exception;
}