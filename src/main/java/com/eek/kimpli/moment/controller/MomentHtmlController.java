package com.eek.kimpli.moment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MomentHtmlController {
     @GetMapping("/moment/list")
    public String list() {
        return "moment/list"; // HTML View를 반환하는 메소드
    }
}
