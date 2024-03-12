package com.finalteam3.midam.controller;

import com.finalteam3.midam.entity.MemberEntity;
import com.finalteam3.midam.entity.TreatSaleEntity;
import com.finalteam3.midam.service.MemberService;
import com.finalteam3.midam.service.TreatSaleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("member")
@Controller
public class MemberController {

    private final MemberService memberService;

    private final TreatSaleService treatSaleService;

    //    회원가입 페이지
    @GetMapping("join")
    public String join() throws Exception {
        return "Join";
    }

    //    회원 가입 시 아이디 중복 체크
    @RequestMapping(value = "join/idCheck")
    @ResponseBody
    public String idCheck(@RequestParam("userId") String userId) throws Exception {
        try {
            userId = memberService.getMemberInfo(userId).getUserId();
            return "중복되는 아이디입니다.";
        } catch (Exception e) {
            e.printStackTrace(); //예외정보받아오기
            return "사용가능한 아이디입니다.";
        }
    }

    //    admin에서 회원등록 시
    @RequestMapping(value = "join/modalIdCheck")
    @ResponseBody
    public String modalIdCheck(@RequestParam("userId") String userId) throws Exception {
        try {
            userId = memberService.getMemberInfo(userId).getUserId();
            return "중복되는 아이디입니다.";
        } catch (Exception e) {
            e.printStackTrace(); //예외정보받아오기
            return "사용가능한 아이디입니다.";
        }
    }

    //    회원등록
    @PostMapping("join")
    public String insertJoin(MemberEntity member) throws Exception {
        memberService.saveMember(member);

        return "redirect:/member/login";
    }

    //    admin에서 회원등록 시
    @RequestMapping(value = "join/joinModal", method = RequestMethod.POST)
    @ResponseBody
    public String joinModal(MemberEntity member) throws Exception {
        memberService.saveMember(member);

        return "회원가입 성공";
    }


    // 로그인 페이지
    @GetMapping("login")
    public String login() throws Exception {
        return "Login";
    }


    //    로그인 처리 페이지
    @PostMapping("login")
    public String memberLogin(@RequestParam("userId") String userId,
                              @RequestParam("userPwd") String userPwd1,
                              @RequestParam(value = "adminYn", required = false, defaultValue = "N") String adminYn,
                              HttpServletRequest req) throws Exception {

        // 아이디 불러오기
        MemberEntity memberEntity = memberService.getMemberInfo(userId);
        // 존재하지 않는 아이디 처리
        if (memberEntity == null) {
            return "redirect:/member/login?errorMsg=" + URLEncoder.encode("존재하지 않는 아이디입니다.", "UTF-8");
        }

        // 로그인 여부 확인 부분에서 memberEntity를 사용하도록 변경
        boolean result = memberService.isMember(userId, userPwd1);

//            관리자
//        회원에 등록되어있는지 확인, 해당 userId가 entity에서 adminYn Y인지, parameter값이 adminYn entity 값이랑 맞는지 확인
        if (result & memberEntity.getAdminYn().equals("Y") & memberEntity.getAdminYn().equals(adminYn)) {

            //            세션 등록
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute("userId", memberEntity.getUserId());
            httpSession.setAttribute("adminYn", memberEntity.getAdminYn());
            httpSession.setAttribute("userName", memberEntity.getUserName());
            httpSession.setAttribute("userPhone", memberEntity.getUserPhone());
            httpSession.setAttribute("userIdx", memberEntity.getUserIdx());
            httpSession.setMaxInactiveInterval(60 * 60 * 10);

            return  "redirect:/main";
        }
        //            일반회원
        else if (result & memberEntity.getAdminYn().equals("N") & memberEntity.getAdminYn().equals(adminYn)) {

//            세션 등록
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute("userId", memberEntity.getUserId());
            httpSession.setAttribute("userGender", memberEntity.getGender());
            httpSession.setAttribute("adminYn", memberEntity.getAdminYn());
            httpSession.setAttribute("userName", memberEntity.getUserName());
            httpSession.setAttribute("userPhone", memberEntity.getUserPhone());
            httpSession.setAttribute("userIdx", memberEntity.getUserIdx());
            httpSession.setMaxInactiveInterval(60 * 60 * 10);

            return "redirect:/main";

        } else{
//        에러 다르게 표시하는 방법 찾기
            return "redirect:/member/login?errorMsg=" + URLEncoder.encode("아이디/비밀번호를 확인해주세요.", "UTF-8");
        }
    }
    //    로그인 : 아이디 찾기
    @GetMapping("find/id")
    public String findUserId() throws Exception {
        return "findUserId";
    }

    @RequestMapping(value = "find/id")
    public ModelAndView findUserId(@RequestParam("userName") String userName, @RequestParam("userPhone") String userPhone) throws Exception {
        ModelAndView mv = new ModelAndView("findUserId");

        MemberEntity member = memberService.findUserId(userName, userPhone);

        if (member == null) {
            mv.addObject("check", 0);
        } else {
            mv.addObject("check", member);
        }

        return mv;
    }


//    로그인 : 비밀번호 찾기

    @GetMapping("find/pwd")
    public String findUserPwd() throws Exception {
        return "findUserPwd";
    }

    @RequestMapping(value = "find/pwd")
    public ModelAndView findUserId(@RequestParam("userId") String userId, @RequestParam("userName") String userName, @RequestParam("userPhone") String userPhone) throws Exception {
        ModelAndView mv = new ModelAndView("findUserPwd");

        MemberEntity member = memberService.findUserPwd(userId, userName, userPhone);

        if (member == null) {
            mv.addObject("check", 0);
        } else {
            /*비밀번호 업데이트*/
            memberService.updateUserPwd(userId);
            member = memberService.getMemberInfo(userId);
            mv.addObject("check", member);
        }

        return mv;
    }


    //     로그아웃 페이지
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest req) throws Exception {
        HttpSession httpSession = req.getSession();

        httpSession.removeAttribute("userId");
        httpSession.removeAttribute("userName");
        httpSession.removeAttribute("userPhone");
        httpSession.removeAttribute("userIdx");
        httpSession.removeAttribute("adminYn");

        httpSession.invalidate();

        return "redirect:/main";

    }

    //    회원정보수정
    @GetMapping("memberEdit")
    public String memberEdit() throws Exception {
        return "memberEdit";
    }

    @PostMapping("memberEdit")
    public String updateMemberDetails(HttpServletRequest req,
                                      @RequestParam("userId") String userId,
                                      @RequestParam(required = false) String userPhone,
                                      @RequestParam(required = false) String userPwd1,
                                      @RequestParam(required = false) String userPwd2) throws Exception {
        memberService.updateMemberDetails(userId, userPhone, userPwd1, userPwd2);
        HttpSession httpSession = req.getSession();
        httpSession.removeAttribute("userPhone");
        httpSession.setAttribute("userPhone", userPhone);

        return "redirect:/main";
    }


    //    고객관리페이지 : 고객리스트 받아오기
    @GetMapping("memberInfoData")
    @ResponseBody
    public List<Object[]> memberInfoData() throws Exception {

        List<Object[]> memberDTOList = memberService.getMemberDTOList();
        return memberDTOList;
    }

    @GetMapping("memberInfo")
    public String memberInfo() throws Exception {



        return "memberInfo";
    }

//    memeberDetail에서 userIdx에 맞는 최근 방문일 불러오기

//    @ResponseBody
//    @GetMapping("/treatList")
//    public List<TreatSaleEntity> treatList(@RequestParam("userIdx") int userIdx) throws Exception{
//        List<TreatSaleEntity> treat = treatSaleService.selectTreatList(userIdx);
//        System.out.println(treat);
//        return treat;
//    }
//    고객검색
@GetMapping("memberSearch")
@ResponseBody
public List<Object[]> memberSearch(@RequestParam("keyword") String keyword) throws Exception {

    List<Object[]> searchMemList = memberService.searchMemberDTOList(keyword);
    return searchMemList;
}

    //    고객관리 페이지 : 고객정보 검색하기
//    @RequestMapping("/memberSearch")
//    public ModelAndView searchUser(@RequestParam("keyword") String keyword) {
//        List<MemberEntity> memberSearch = memberService.searchUser(keyword);
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("/customer/member/memberInfo");
//        modelAndView.addObject("memberSearch", memberSearch);
//
//        return modelAndView;
//    }


    //    고객관리페이지->고객디테일로 이동
    @RequestMapping("memberDetail")
    public ModelAndView memberDetail(@RequestParam("userIdx") int userIdx) throws Exception {

        List<MemberEntity> detail = memberService.getMemberDetail(userIdx);
        List<TreatSaleEntity> treat = treatSaleService.getTreatList(userIdx);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("memberDetail");
        mv.addObject("detail", detail);
        mv.addObject("treat", treat);

        return mv;
    }

    //    시술내역 ajax로 불러오기
    @ResponseBody
    @GetMapping("memberTreatDetail")
    public List<TreatSaleEntity> memberTreatDetail(@RequestParam("userIdx") int userIdx) throws Exception {
        List<TreatSaleEntity> treat = treatSaleService.getTreatList(userIdx);

        return treat;
    }

    //    관리자화면에서 회원 메모 수정하기
    @RequestMapping(value = "editMemo", method = RequestMethod.POST)
    @ResponseBody
    public String editMemo(@RequestParam("userIdx") int userIdx, @RequestParam("userMemo") String userMemo) throws Exception {

        memberService.editMemo(userIdx, userMemo);

        return "회원 정보가 수정되었습니다.";

    }


}
