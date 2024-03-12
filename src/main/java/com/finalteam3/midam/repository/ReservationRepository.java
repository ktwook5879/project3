package com.finalteam3.midam.repository;

import com.finalteam3.midam.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
    @Query("SELECT r.date, COUNT(r) FROM ReservationEntity r WHERE r.date BETWEEN :start AND :end GROUP BY r.date")
    List<Object[]> countReservationsByDateBetween(@Param("start") String start, @Param("end") String end);

    List<ReservationEntity> findByDate(String reservationDate);

    List<ReservationEntity> findAllByOrderByDateDesc();

    void deleteById(Integer idx);

    List<ReservationEntity> findByUserIdxOrderByDateDesc(String userIdx);
}
