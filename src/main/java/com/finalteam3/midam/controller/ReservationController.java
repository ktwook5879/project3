package com.finalteam3.midam.controller;

import com.finalteam3.midam.entity.ReservationEntity;
import com.finalteam3.midam.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("reservation")
@Controller
public class ReservationController {

    private final ReservationService reservationService;

    @RequestMapping("")
    public String index() throws Exception {
        return "adminMain";
    }

    @GetMapping("reservationPage")
    public ModelAndView selectReservationLists(HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView("reservationPage");

        // HttpSession에서 userIdx 가져오기
        HttpSession session = request.getSession();
        Integer userIdxInteger = (Integer) session.getAttribute("userIdx");

        // Integer를 String으로 변환
        String userIdx = String.valueOf(userIdxInteger);

        // userIdx로 예약 내역 필터링
        List<ReservationEntity> reservationList = reservationService.findByUserIdxOrderByDateDesc(userIdx);
        List<ReservationEntity> futureReservations = getFutureReservations(reservationList);

        mv.addObject("reservationList", futureReservations);
        mv.addObject("pastReservations", getPastReservations(reservationList));

        return mv;
    }
    //    필터링 관련 코드
    private List<ReservationEntity> getFutureReservations(List<ReservationEntity> reservations) {
        ZonedDateTime now = ZonedDateTime.now();
        return reservations.stream()
                .filter(reservation -> reservation.getLocalDateTime().isAfter(now.toLocalDateTime()))
                .collect(Collectors.toList());
    }
    //    필터링 관련 코드
    private List<ReservationEntity> getPastReservations(List<ReservationEntity> reservations) {
        ZonedDateTime now = ZonedDateTime.now();
        return reservations.stream()
                .filter(reservation -> reservation.getLocalDateTime().isBefore(now.toLocalDateTime()))
                .collect(Collectors.toList());
    }

    @RequestMapping("timeTable")
    public String timeTable() throws Exception {
        return "timeTable";
    }

    @ResponseBody
    @RequestMapping("getReservedTimes")
    public List<ReservationEntity> getReservedTimes(@RequestParam String reservationDate) throws Exception {
        List<ReservationEntity> data = reservationService.getReservationsByDate(reservationDate);
        return data;
    }

    @RequestMapping("calendar")
    public String calendar() throws Exception {
        return "reservationCalendar";
    }

    @ResponseBody
    @RequestMapping("count")
    public List<Object[]> getReservationCountsByDateRange(@RequestParam String start, @RequestParam String end) throws Exception {
        List<Object[]> data =reservationService.getReservationCountsByDateRange(start, end);
        return data;
    }

    @ResponseBody
    @PostMapping(value = "insert")
    public void insertReservation(@RequestBody ReservationEntity reservation)throws Exception {
        reservationService.saveReservation(reservation);
    }

    @ResponseBody
    @PostMapping(value = "customInsert")
    public void customInsert(@RequestBody ReservationEntity reservation)throws Exception {
        reservationService.saveReservation(reservation);
    }

    @ResponseBody
    @GetMapping("events")
    public List<ReservationEntity> findAll()throws Exception {
        List<ReservationEntity> data = reservationService.findAllByOrderByDateDesc();
        return data;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable("id") Integer reservationId)throws Exception {
        reservationService.deleteReservation(reservationId);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateReservation(@RequestBody ReservationEntity updatedReservation) throws Exception {
        reservationService.updateReservation(updatedReservation);
    }


}
