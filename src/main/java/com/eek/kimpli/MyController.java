package com.eek.kimpli;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
//@Controller
public class MyController {

//    	    	@GetMapping("/chat")
//			public String chat(){
//				return "chat/chat";
//			}
@GetMapping("/chat")
public String chat(Model model, @RequestParam(name = "userId") String userId) {
    model.addAttribute("userId", userId); // userId를 모델에 추가
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("userSession", authentication.getPrincipal());
    return "chat/chat";
}

//                	@GetMapping("/chat")
//			public String chat(){
//				return "chat/chat";
//			}

//            	@GetMapping("/toto")
//			public String apple(){
//				return "chat/apple";
//			}

//    @PostMapping("/login1")
//    public String checkAuthentication(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.isAuthenticated()) {
//            String username = authentication.getName();
//            System.out.println("인증된 사용자: " + username);
//
//            // 사용자의 권한 가져오기
//            authentication.getAuthorities().forEach(authority -> {
//            System.out.println("사용자 권한: " + authority.getAuthority());
//
//            model.addAttribute("username", username);
//            model.addAttribute("authorities", authority.getAuthority());
//            });
//
//            return "/index";
//        } else {
//            return "사용자는 인증되지 않았습니다.";
//        }
//    }
}