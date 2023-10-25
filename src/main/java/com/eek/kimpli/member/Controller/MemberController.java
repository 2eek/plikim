package com.eek.kimpli.member.Controller;

import com.eek.kimpli.member.dto.MemberDTO;
import com.eek.kimpli.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor//생성자 주입(Constructor Injection)을 간편하게 만들기 위해 사용
@RequestMapping("/member")//공통주소
public class MemberController {
     //생성자 주입방식으로 의존성주입받게됨
    private  final MemberService memberService;

    //가입한 회원 리스트 조회. 단순한 호출 GetMapping을 이용한다. URL에 매핑된 핸들러 필요하다
    @GetMapping("/memberlist")
    public String saveForm(){
        return "member/memberlist";
    }

    //회원가입폼 호춯
    @GetMapping("/memberjoin")
    public String joinForm(){
        return "member/memberjoinform";
    }

    //회원가입 처리.
    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        int saveResult = memberService.save(memberDTO);
        if(saveResult > 0){
            return "redirect:/";
        }else {
            return "redirect:member/memberjoinform";
        }
    }

    @GetMapping("/loginForm")
    public String loginFotm(){
        return "member/login";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        boolean loginResult = memberService.login(memberDTO);
        if(loginResult){
            session.setAttribute("memberEmail", memberDTO.getId());
            return  "redirect:/";
        }else {
            return "member/login";


        }

    }
}

