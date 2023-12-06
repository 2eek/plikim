package com.eek.kimpli.mail.controller;

import com.eek.kimpli.mail.Service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/sendemail")
    public String MailPage(){
        return "email/Mail";
    }

    @ResponseBody
    @PostMapping("/mail")
    public String MailSend(String mail){

       int number = mailService.sendMail(mail);

       String num = "" + number;
        System.out.println("번호뭔데"+number);
       return num;
    }

}
