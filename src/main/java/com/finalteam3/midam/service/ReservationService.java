package com.finalteam3.midam.service;

import com.finalteam3.midam.entity.ReservationEntity;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ReservationService {
    List<Object[]> getReservationCountsByDateRange(String start, String end) throws Exception;

    void saveReservation(ReservationEntity reservation) throws Exception;

    List<ReservationEntity> findAllByOrderByDateDesc() throws Exception;

    void deleteReservation(Integer eventId) throws Exception;

    void updateReservation(ReservationEntity updatedReservation) throws Exception;

    List<ReservationEntity> getReservationsByDate(String reservationDate) throws Exception;

    List<ReservationEntity> findByUserIdxOrderByDateDesc(String userIdx);

}
