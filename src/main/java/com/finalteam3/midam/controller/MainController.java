package com.finalteam3.midam.controller;

import com.finalteam3.midam.entity.ReservationEntity;
import com.finalteam3.midam.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("main")
@Controller
public class MainController {

    private final ReservationService reservationService;

    @GetMapping("")
    public ModelAndView selectReservationList(HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView("mainPage");

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

    @PostMapping("delete")
    public String deleteReservation(@RequestParam Integer idx) {
        try {
            reservationService.deleteReservation(idx);
            return "redirect:/reservation/reservationPage";
        } catch (Exception e) {
            // 예외 처리 로직 추가
            return "errorPage"; // 에러 페이지로 리다이렉트 또는 에러 메시지를 보여줄 수 있음
        }
    }

    @GetMapping("errorPage")
    public String errorPage() throws Exception{
        return "errorPage";
    }
}

