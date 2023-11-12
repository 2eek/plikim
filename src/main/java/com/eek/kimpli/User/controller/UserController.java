package com.eek.kimpli.User.controller;

import com.eek.kimpli.User.model.User;
import com.eek.kimpli.User.repository.UserRepository;
import com.eek.kimpli.User.service.UserService;
import com.eek.kimpli.board.model.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class UserController {

    final UserService userService;
    final UserRepository userRepository;

//    @GetMapping("/")
//    public String list(Model model){
//        List<User> users = userRepository.findAll();
//        model.addAttribute("user",users);
//        return "index";
//    }


    //페이징+검색
    @GetMapping("/")
    public String list(Model model, @PageableDefault(size = 3) Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        int currentPage = users.getPageable().getPageNumber() + 1; // 현재 페이지 번호 (0부터 시작)
        int startPage = Math.max(1, currentPage - 2); // 현재 페이지 주변에 2 페이지씩 보여주기
        int endPage = Math.min(users.getTotalPages(), startPage + 4);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("user", users);
         return "index";
    }






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
