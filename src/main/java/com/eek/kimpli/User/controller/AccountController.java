package com.eek.kimpli.User.controller;

import com.eek.kimpli.User.model.User;
import com.eek.kimpli.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@RequiredArgsConstructor
@Controller
public class AccountController {

    @Autowired
    private UserService userService;



       //시큐리티 로그인 테스트
//    @PostMapping("/login11")
//        public String login(User user){
//        userService.save(user);
//            return "/";
//        }

        //회원가입
        @PostMapping("/member/saveTest")
    public String JoinTest(@ModelAttribute("user") User user){
            System.out.println("회원가입되나");
        userService.save(user);
        System.out.println("회원가입안되나");
            return "/index";
        }

//        	    	@GetMapping("/chat")
//			public String chat(){
//				return "/chat/chat";
//			}
}
