package com.finalteam3.midam.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

// Interceptor을 사용하기 위해서 HandlerInterceptor 인터페이스를 상속받아 구현함
public class LoginCheck implements HandlerInterceptor {
//  preHandle() : 지정한 컨트롤러가 동작하기 이전에 먼저 수행됨
//  postHandle() : 지정한 컨트롤러가 동작 후 view가 동작하기 이전에 수행됨
//  afterCompletion() : 모든 동작이 완료된 후 수행됨

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {

//    현재 세션 정보를 가져옴
        HttpSession session = req.getSession();

        System.out.println("\n========== interceptor 동작 ==========\n");

//    세션에 저장된 내용 중 사용자 ID 정보를 가져옴
        String userId = (String) session.getAttribute("userId");
        System.out.println("USER ID : " + userId);

//    로그인 상태인지 아닌지 확인
        if (userId == null || userId.equals("")) {
//      비 로그인 상태
            System.out.println("********** interceptor **********");
            System.out.println("비 로그인 상태");
            System.out.println("User ID : " + (String) session.getAttribute("userId"));


//      로그인 페이지로 이동
            res.sendRedirect("/main/errorPage");
//      인터셉터를 통과하지 못하였으므로 false를 리턴, 지정한 컨트롤러에 접근 불가
            return false;
        }
        else {
//      로그인 상태
            System.out.println("********** interceptor **********");
            System.out.println("로그인 상태");
            System.out.println("User ID : " + (String) session.getAttribute("userId"));

//      현재 세션의 사용 시간을 초기화
            session.setMaxInactiveInterval(60*60);
//      인터셉터를 통과했으므로 true를 리턴, 지정한 컨트롤러에 접근 가능
            return true;
        }
    }
}