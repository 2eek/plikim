package com.eek.kimpli.member.Controller;

import com.eek.kimpli.member.dto.MemberDTO;
import com.eek.kimpli.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor//생성자 주입(Constructor Injection)을 간편하게 만들기 위해 사용
@RequestMapping("/member")//공통주소
public class MemberController {
     //생성자 주입방식으로 의존성주입받게됨
    private  final MemberService memberService;

    //단순한 호출 GetMapping을 이용한다. URL에 매핑된 핸들러 필요하다.
    @GetMapping("/memberlist")
    public String saveForm(){
        return "member/memberlist";}

    @GetMapping("/memberjoin")
    public String joinForm(){
        return "member/memberjoinform";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        int saveResult = memberService.save(memberDTO);
        if(saveResult > 0){
            return "index";
        }else {
            return "member/memberjoinform";
        }
    }
}
