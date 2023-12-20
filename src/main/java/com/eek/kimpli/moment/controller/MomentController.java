package com.eek.kimpli.moment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MomentController {

    @GetMapping("/moment/list")
    public String list (){
        return "/moment/list";
    }
}
