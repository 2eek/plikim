package com.eek.kimpli.moment.controller;

import com.eek.kimpli.moment.model.Moment;
import com.eek.kimpli.moment.model.MomentImg;
import com.eek.kimpli.moment.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/moment/list")
public String list(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("userSession", authentication.getPrincipal());
        System.out.println("인증모멘트 뭐있나"+authentication.getPrincipal());
    List<Moment> moments = momentService.findAll();
    model.addAttribute("moments", moments);
    return "moment/list";
}




}
