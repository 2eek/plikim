package com.eek.kimpli.kakaoLogin.controller;

import com.eek.kimpli.kakaoLogin.service.KakaoLoginServiceImpl;
import com.eek.kimpli.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class KakaoLoginController {
    private final KakaoLoginServiceImpl ks;


  @GetMapping("/kakaologin")
public String kakaoLogin(@RequestParam(value = "code", required = false) String code, Model model) throws Exception {
    Map<String, String> map = ks.getAccessToken(code);
    String access_Token = map.get("access_token");
    String refresh_Token = map.get("refresh_token");

    User kakaoUser = ks.getUserInfo(access_Token, refresh_Token);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();

   if (principal instanceof Map) {
        // principal이 Map인 경우, Map에서 username을 추출
        String userName = ((Map<String, String>) principal).get("username");
        model.addAttribute("userSession", userName);
        System.out.println("사용자 아이디: " + userName);
    } else {
        System.out.println("UserDetails가 아닌 Principal입니다: " + principal);
    }

    return "redirect:/";
}

}








