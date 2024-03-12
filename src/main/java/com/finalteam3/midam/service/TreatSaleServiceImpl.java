package com.finalteam3.midam.service;

import com.finalteam3.midam.entity.TreatSaleEntity;
import com.finalteam3.midam.repository.TreatSaleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TreatSaleServiceImpl implements TreatSaleService {

    private  final TreatSaleRepository treatSaleRepository;
    @Override
    public void saveSale(TreatSaleEntity sale) throws Exception {
        treatSaleRepository.save(sale);
    }

    //    memberDetail페이지에서 시술내역 띄워주기
    @Override
    public List<TreatSaleEntity> getTreatList(int userIdx) throws Exception {
        return treatSaleRepository.findByUserIdxOrderByPaymentDayDesc(userIdx);
    }

    @Override
    public List<TreatSaleEntity> getSalesByPaymentDay(String date) {
        return treatSaleRepository.findByPaymentDay(date);
    }


    @Override
    public List<TreatSaleEntity> getSalesByPaymentRange(String startDate, String endDate) {
        return treatSaleRepository.findByPaymentDayBetweenOrderByPaymentDayDescPaymentTimeDesc(startDate, endDate);
    }

    @Override
    @Transactional
    public void deleteSale(Integer saleId) {
        treatSaleRepository.deleteByIdx(saleId);
    }

    @Override
    public List<Object[]> getCashAndCreditTotalByDateRange(String startDate, String endDate) {
        return treatSaleRepository.getCashAndCreditTotalByDateRange(startDate, endDate);
    }

    @Override
    public Object[] findCashAndCreditTotalsByDateRange(String startDate, String endDate) {
        return treatSaleRepository.findCashAndCreditTotalsByDateRange(startDate, endDate);
    }

    @Override
    public List<Object[]> getCashAndCreditTotalByMonth(int year) {
        return treatSaleRepository.getCashAndCreditTotalByMonth(year);
    }
}
