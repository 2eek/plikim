package com.eek.kimpli.User.controller;

import com.eek.kimpli.User.model.User;
import com.eek.kimpli.User.repository.UserRepository;
import com.eek.kimpli.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    //마이페이지 조회
    @GetMapping("/member/mypage")
   public String showDashboard(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // 현재 사용자의 세션 정보
    model.addAttribute("userSession", authentication.getPrincipal());
    return "member/mypage";
}

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

    //회원 디테일
//    @GetMapping("/user/userdetail/{id}")
//    public String list(Model model, @RequestParam(required = false) Long id) {
//       User user = userRepository.findById(id).orElse(null);
//       model.addAttribute("user", user);
//    return "member/userdetail";
//    }
@GetMapping("/user/userdetail")
public String getUserDetail(@RequestParam(required = false) Long id, Model model) {
    // 사용자 정보를 id를 기반으로 데이터베이스에서 가져오는 코드 작성
    User userdetail = userRepository.findById(id).orElse(null);
    if (userdetail == null) {
        // 사용자 정보가 없을 경우 적절한 처리
        return "redirect:/error"; // 예를 들어, 오류 페이지로 리다이렉트
    }
    model.addAttribute("user", userdetail);
    //로그인 회원 세션 활용
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // 현재 사용자의 세션 정보
    model.addAttribute("userSession", authentication.getPrincipal());
    return "member/userdetail"; // 사용자 정보가 있는 경우 상세 정보 페이지로 이동
}




       //시큐리티 로그인 테스트
//    @PostMapping("/login11")
//        public String login(User user){
//        userService.save(user);
//            return "/";
//        }

        //회원가입
        @PostMapping("/member/saveTest")
    public String JoinTest(@ModelAttribute("user") User user, Model model, @PageableDefault(size = 3) Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        int currentPage = users.getPageable().getPageNumber() + 1; // 현재 페이지 번호 (0부터 시작)
        int startPage = Math.max(1, currentPage - 2); // 현재 페이지 주변에 2 페이지씩 보여주기
        int endPage = Math.min(users.getTotalPages(), startPage + 4);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("user", users);
            System.out.println("회원가입되나");
        userService.save(user);
        System.out.println("회원가입안되나");
            return "redirect:/";
        }
//
//           @GetMapping("/")
//    public String list(Model model, @PageableDefault(size = 3) Pageable pageable) {
//        Page<User> users = userRepository.findAll(pageable);
//        int currentPage = users.getPageable().getPageNumber() + 1; // 현재 페이지 번호 (0부터 시작)
//        int startPage = Math.max(1, currentPage - 2); // 현재 페이지 주변에 2 페이지씩 보여주기
//        int endPage = Math.min(users.getTotalPages(), startPage + 4);
//        model.addAttribute("startPage",startPage);
//        model.addAttribute("endPage",endPage);
//        model.addAttribute("user", users);
//         return "index";
//    }





//        	    	@GetMapping("/chat")
//			public String chat(){
//				return "/chat/chat";
//			}
}
