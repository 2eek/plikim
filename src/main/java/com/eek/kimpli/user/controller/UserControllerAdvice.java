package com.eek.kimpli.user.controller;

// UserController.java

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class UserControllerAdvice {

//    @ExceptionHandler(DuplicateUserDataException.class)
//    public String handleDuplicateUserDataException(DuplicateUserDataException e, Model model) {
//        // 중복된 값이 있을 때 처리
//        model.addAttribute("error", e.getMessage());
//        System.out.println("예외처리 메세지"+e.getMessage());
//        return "redirect:/member/memberjoin";
//    }
    @ExceptionHandler(DuplicateUserDataException.class)
public String handleDuplicateUserDataException(DuplicateUserDataException e, Model model, RedirectAttributes attributes) {
    // 중복된 값이 있을 때 처리
    model.addAttribute("error", e.getMessage());
 System.out.println("예외처리 메세지?"+e.getMessage());
    // 폼 데이터를 유지합니다.
    attributes.addFlashAttribute("formData", model.asMap());
        System.out.println("asMap??"+model.asMap());

    return "redirect:/user/memberjoin";
}
}

