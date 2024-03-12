package com.finalteam3.midam.service;

import com.finalteam3.midam.entity.TreatSaleEntity;

import java.util.List;
import java.util.Map;

public interface TreatSaleService {

    void saveSale(TreatSaleEntity treatSaleEntity) throws Exception ;

    //    memberDetail페이지에서 시술내역 띄워주기
    List<TreatSaleEntity> getTreatList(int userIdx) throws Exception;


    List<TreatSaleEntity> getSalesByPaymentDay(String date);
    List<TreatSaleEntity> getSalesByPaymentRange(String startDate, String endDate);

    void deleteSale(Integer saleId);

    List<Object[]> getCashAndCreditTotalByDateRange(String startDate, String endDate);

    Object[] findCashAndCreditTotalsByDateRange(String startDate, String endDate);

    List<Object[]> getCashAndCreditTotalByMonth(int year);
}
