package com.finalteam3.midam.repository;

import com.finalteam3.midam.entity.TreatSaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TreatSaleRepository extends JpaRepository<TreatSaleEntity, Integer> {

    //    memberDetail페이지에서 시술내역 띄워주기
    List<TreatSaleEntity> findByUserIdxOrderByPaymentDayDesc(int userIdx) throws Exception;

    List<TreatSaleEntity> findByPaymentDay(String paymentDay);

    List<TreatSaleEntity> findByPaymentDayBetweenOrderByPaymentDayDescPaymentTimeDesc(String startDate, String endDate);

    void deleteByIdx(Integer saleId);

    @Query("SELECT SUBSTRING(t.paymentDay, 1, 10) AS date, " +
            "SUM(CASE WHEN t.paymentMethod = 'credit' THEN t.payment ELSE 0 END) AS creditTotal, " +
            "SUM(CASE WHEN t.paymentMethod = 'cash' THEN t.payment ELSE 0 END) AS cashTotal, " +
            "SUM(t.payment) AS total " +
            "FROM TreatSaleEntity t " +
            "WHERE t.paymentDay BETWEEN :startDate AND :endDate " +
            "GROUP BY SUBSTRING(t.paymentDay, 1, 10) " +
            "ORDER BY SUBSTRING(t.paymentDay, 1, 10) DESC")
    List<Object[]> getCashAndCreditTotalByDateRange(@Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);


    @Query("SELECT SUM(CASE WHEN t.paymentMethod = 'cash' THEN t.payment ELSE 0 END), " +
            "SUM(CASE WHEN t.paymentMethod = 'credit' THEN t.payment ELSE 0 END), " +
            "SUM(t.payment) " +
            "FROM TreatSaleEntity t " +
            "WHERE t.paymentDay BETWEEN :startDate AND :endDate")
    Object[] findCashAndCreditTotalsByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);



//    @Query("SELECT SUBSTRING(t.paymentDay, 1, 4), SUBSTRING(t.paymentDay, 6, 2), " +
//            "SUM(CASE WHEN t.paymentMethod = 'credit' THEN t.payment ELSE 0 END), " +
//            "SUM(CASE WHEN t.paymentMethod = 'cash' THEN t.payment ELSE 0 END), " +
//            "SUM(t.payment) " +
//            "FROM TreatSaleEntity t " +
//            "WHERE t.paymentDay LIKE CONCAT(:year, '-%') " +
//            "GROUP BY SUBSTRING(t.paymentDay, 1, 4), SUBSTRING(t.paymentDay, 6, 2)")



    @Query("SELECT SUBSTRING(t.paymentDay, 6, 2), " +
            "SUM(CASE WHEN t.paymentMethod = 'cash' THEN t.payment ELSE 0 END), " +
            "SUM(CASE WHEN t.paymentMethod = 'credit' THEN t.payment ELSE 0 END), " +
            "SUM(t.payment), " +
            "(SUM(t.payment) / COUNT(DISTINCT SUBSTRING(t.paymentDay, 6, 2))), " +
            "(COUNT(DISTINCT t.userName)), " +
            "(SUM(t.payment) / COUNT(DISTINCT t.userName)) " +
            "FROM TreatSaleEntity t " +
            "WHERE t.paymentDay LIKE CONCAT(:year, '-%') " +
            "GROUP BY SUBSTRING(t.paymentDay, 6, 2)"+
            "ORDER BY SUBSTRING(t.paymentDay, 6, 2) DESC")
    List<Object[]> getCashAndCreditTotalByMonth(int year);

}
