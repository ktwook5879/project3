package com.finalteam3.midam.service;

import com.finalteam3.midam.entity.TreatEntity;
import com.finalteam3.midam.repository.TreatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class TreatServiceImpl implements TreatService{

    private final TreatRepository treatRepository;

    //    시술메뉴 불러오기
    @Override
    public List<TreatEntity> selectTreatList() throws Exception {
        return treatRepository.findAllByOrderByTreatAmount();
    }

    //    시술메뉴 추가하기
    @Override
    public void treatSave(TreatEntity treatEntity) throws Exception {
        treatRepository.save(treatEntity);
    }

    //    시술메뉴 수정위해 기존 데이터 불러오기
    @Override
    public TreatEntity findTreatList(int treatIdx) {
        return treatRepository.findByTreatIdx(treatIdx);
    }

    //    시술메뉴 수정하기
    @Override
    public void updateTreatList(int treatIdx, String treatName, String treatTimeHour, String treatTimeMinute, int treatAmount, String gender) {
        treatRepository.updateTreatList(treatIdx,treatName,treatTimeHour,treatTimeMinute,treatAmount,gender);
    }

    //    시술메뉴 삭제하기
    @Override
    public void deleteTreatList(int treatIdx) {
        treatRepository.deleteById(treatIdx);
    }
    @Override
    public List<TreatEntity> findAllSortedByGenderAndTreatAmount() {
        return treatRepository.findAllByOrderByGenderAscTreatAmountAsc();
    }


    @Override
    public List<TreatEntity> getTreatsByGender(String gender) {
        return treatRepository.findByGender(gender);
    }
}
