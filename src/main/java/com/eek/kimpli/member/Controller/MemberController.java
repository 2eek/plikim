package com.eek.kimpli.member.Controller;

import com.eek.kimpli.member.dto.MemberDTO;
//import com.eek.kimpli.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor//생성자 주입(Constructor Injection)을 간편하게 만들기 위해 사용
@RequestMapping("/member")//공통주소
public class MemberController {
     //생성자 주입방식으로 의존성주입받게됨
//    private  final MemberService memberService;


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

//    //회원가입 처리.
//    @PostMapping("/save")
//    public String save(@ModelAttribute MemberDTO memberDTO){
//        int saveResult = memberService.save(memberDTO);
//        if(saveResult > 0){
//            return "redirect:/";
//        }else {
//            return "redirect:member/memberjoinform";
//        }
//    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "member/login";
    }

    //로그인 시 계정 정보가 틀렸다면, url을 시큐리티가 처리해준다. action이 post이지만 시큐리티가 get으로 처리해줌 + 타임리프가 error받아서 화면에 띄워줌
//     @GetMapping("/login1")
//    public String loginForm1(){
//        return "member/login";
//    }
//    @PostMapping("/login1")//시큐리티에서 처리할 예
//    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
//        boolean loginResult = memberService.login(memberDTO);
//        if(loginResult == true ){
//            session.setAttribute("memberEmail", memberDTO.getMemberEmail());
//            return  "redirect:/";
//            // return "member/login";
//        }else {
//            return "member/login";
//            //return  "redirect:/";
//
//
//        }
//
//    }


//      @GetMapping("/logintest")
//    public String logintest(){
//        return "member/logintest";
//    }
//      @GetMapping("/jointest")
//    public String jointest(){
//        return "member/jointest";
//    }







}

