package com.eek.kimpli.moment.controller;

import com.eek.kimpli.moment.model.Moment;
import com.eek.kimpli.moment.model.MomentImg;
import com.eek.kimpli.moment.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MomentHtmlController {

    public MomentHtmlController(MomentService momentService) {
        this.momentService = momentService;
    }

    private MomentService momentService;

//    @GetMapping("/moment/list")
//    public String list(Model model) {
////        List<Moment> moments = momentService.getAllMoments();
//        List<Moment> moments = momentService.findAll();
//        System.out.println("뭐있나?모멘츠에"+moments);
//
//        model.addAttribute("moments", moments);
//        return "moment/list"; // HTML View를 반환하는 메소드
//    }
    @GetMapping("/moment/list")
public String list(Model model) {
    List<Moment> moments = momentService.findAll();
    model.addAttribute("moments", moments);
    return "moment/list";
}
}
