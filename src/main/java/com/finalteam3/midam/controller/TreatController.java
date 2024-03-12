package com.finalteam3.midam.controller;

import com.finalteam3.midam.entity.TreatEntity;
import com.finalteam3.midam.service.TreatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("treat")
@Controller
public class TreatController {

    private final TreatService treatService;


    //    시술메뉴
    @GetMapping("treatList")
    public ModelAndView treatList() throws Exception {
//        시술메뉴 불러와서 띄워주기
        ModelAndView mv = new ModelAndView("treatList");

        List<TreatEntity> treatList = treatService.selectTreatList();

        mv.addObject("treatList", treatList);

        return mv;
    }

    //    시술메뉴추가 페이지로 이동
    @GetMapping("treatList/treatWrite")
    public String treatWrite(@RequestParam("gender") String gender, Model model) throws Exception {
        model.addAttribute("gender", gender);
        return "treatWrite";
    }

    //     시술메뉴추가 처리
    @PostMapping(value = "treatList/treatWrite")
    public String treatWrite(TreatEntity treatEntity) throws Exception {

        treatService.treatSave(treatEntity);

        return "redirect:/treat/treatList";
    }

    //    시술메뉴 편집페이지로 이동
    @RequestMapping("treatList/treatEdit")
    public ModelAndView treatEdit(@RequestParam int treatIdx) throws Exception {
        ModelAndView mv = new ModelAndView("treatEdit");

        TreatEntity treat = treatService.findTreatList(treatIdx);

        mv.addObject("treat", treat);

        return mv;
    }

    //     시술메뉴 편집처리
    @PostMapping(value = "treatList/treatEdit")
    public String treatEdit(@RequestParam("treatIdx") int treatIdx, @RequestParam("treatName") String treatName, @RequestParam("treatTimeHour") String treatTimeHour, @RequestParam("treatTimeMinute") String treatTimeMinute, @RequestParam("treatAmount") int treatAmount, @RequestParam("gender") String gender) throws Exception {

        treatService.updateTreatList(treatIdx, treatName, treatTimeHour, treatTimeMinute, treatAmount, gender);

        return "redirect:/treat/treatList";
    }

    //    시술메뉴 삭제
    @RequestMapping("treatList/treatDelete")
    public String treatDelete(@RequestParam("treatIdx") int treatIdx) throws Exception {
        treatService.deleteTreatList(treatIdx);
        return "redirect:/treat/treatList";
    }

    //        시술메뉴 불러와서 띄워주기
    @ResponseBody
    @GetMapping("treatListBody")
    public List<TreatEntity> treatListBody() throws Exception{

        List<TreatEntity> treatList = treatService.findAllSortedByGenderAndTreatAmount();

        return treatList;
    }

    //        성별을 받아서 시술메뉴 불러와서 띄워주기
    @ResponseBody
    @GetMapping("treatListByGender")
    public List<TreatEntity> treatListByGender(@RequestParam("gender") String gender) throws Exception{

        List<TreatEntity> treatList = treatService.getTreatsByGender(gender);

        return treatList;
    }
}
