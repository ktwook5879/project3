package com.finalteam3.midam.controller;

import com.finalteam3.midam.entity.MemberEntity;
import com.finalteam3.midam.entity.TreatSaleEntity;
import com.finalteam3.midam.service.MemberService;
import com.finalteam3.midam.service.TreatSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("sale")
@Controller
public class TreatSaleController {

    private final TreatSaleService treatSaleService;

    private final MemberService memberService;

    @GetMapping("saleManagement")
    public String saleManagement() throws Exception {
//        매출관리(일간,월간,연간)
        return "salesManagement";
    }

    @GetMapping("addSales")
    public String addSales() throws Exception {
        return "addSales";
    }


    @GetMapping("salesPut")
    public String salesPut() throws Exception {
//        매출등록
        return "salesPut";
    }

    @PostMapping("insert")
    public String insertMemberHistory(TreatSaleEntity treatSaleEntity) throws Exception {
        treatSaleService.saveSale(treatSaleEntity);

        return "redirect:/sale/salesPut";
    }

    @ResponseBody
    @RequestMapping(value = "saleMemberSearch", method = RequestMethod.POST)
    public Object saleMemberSearch(@RequestParam("userName") String userName) {
        List<MemberEntity> salesUser = memberService.searchSalesUser(userName);

        return salesUser;
    }

    @ResponseBody
    @GetMapping("date")
    public List<TreatSaleEntity> getSalesByPaymentDay(@RequestParam("date") String date) {
        List<TreatSaleEntity> data = treatSaleService.getSalesByPaymentDay(date);
        return data;
    }

    @ResponseBody
    @GetMapping("getSalesByDateRange")
    public List<TreatSaleEntity> getSalesByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        List<TreatSaleEntity> data = treatSaleService.getSalesByPaymentRange(startDate, endDate);
        return data;
    }

    @ResponseBody
    @DeleteMapping("deleteSale/{saleId}")
    public void deleteSale(@PathVariable Integer saleId) {
        treatSaleService.deleteSale(saleId);
    }

    @ResponseBody
    @GetMapping("getCashAndCreditTotal")
    public List<Object[]> getCashAndCreditTotalByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<Object[]> cashAndCreditTotal = treatSaleService.getCashAndCreditTotalByDateRange(startDate, endDate);
        return cashAndCreditTotal;
    }

    @ResponseBody
    @GetMapping("totals")
    public Object[] findCashAndCreditTotalsByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    ) {
        Object[] totals = treatSaleService.findCashAndCreditTotalsByDateRange(startDate, endDate);
        return totals;
    }

    @ResponseBody
    @GetMapping("monthly")
    public List<Object[]> getCashAndCreditTotalByMonth(@RequestParam("year") int year) {
        List<Object[]> data = treatSaleService.getCashAndCreditTotalByMonth(year);
        return data;
    }
}