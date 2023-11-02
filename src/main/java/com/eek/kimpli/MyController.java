package com.eek.kimpli;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
//@Controller
public class MyController {

    @PostMapping("/login1")
    public String checkAuthentication(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println("인증된 사용자: " + username);

            // 사용자의 권한 가져오기
            authentication.getAuthorities().forEach(authority -> {
                System.out.println("사용자 권한: " + authority.getAuthority());

                    model.addAttribute("username", username);
            model.addAttribute("authorities", authority.getAuthority());
            });

            return "/index";
        } else {
            return "사용자는 인증되지 않았습니다.";
        }
    }
}