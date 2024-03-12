package com.finalteam3.midam.service;

import com.finalteam3.midam.entity.ReservationEntity;
import com.finalteam3.midam.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public List<Object[]> getReservationCountsByDateRange(String start, String end) {
        return reservationRepository.countReservationsByDateBetween(start, end);
    }

    @Override
    public void saveReservation(ReservationEntity reservation) throws Exception {
        reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationEntity> findAllByOrderByDateDesc() throws Exception{
        return reservationRepository.findAllByOrderByDateDesc();
    }

    @Override
    public void deleteReservation(Integer eventId) {
        reservationRepository.deleteById(eventId);
    }

    @Override
    public void updateReservation(ReservationEntity updatedReservation) {
        ReservationEntity reservation = reservationRepository.findById(updatedReservation.getIdx())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + updatedReservation.getIdx()));

        // 업데이트할 정보만 업데이트
        reservation.setName(updatedReservation.getName());
        reservation.setStartTime(updatedReservation.getStartTime());
        reservation.setEndTime(updatedReservation.getEndTime());
        reservation.setDate(updatedReservation.getDate());
        reservation.setContent(updatedReservation.getContent());
        reservation.setPhoneNum(updatedReservation.getPhoneNum());
        reservation.setMemo(updatedReservation.getMemo());

        reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationEntity> getReservationsByDate(String reservationDate) {
        return reservationRepository.findByDate(reservationDate);
    }

    @Override
    public List<ReservationEntity> findByUserIdxOrderByDateDesc(String userIdx) {
        return reservationRepository.findByUserIdxOrderByDateDesc(userIdx);
    }
}