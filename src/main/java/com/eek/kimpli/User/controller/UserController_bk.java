//package com.eek.kimpli.User.controller;
//
//import com.eek.kimpli.User.model.User;
//import com.eek.kimpli.User.repository.UserRepository;
//import com.eek.kimpli.User.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@RequiredArgsConstructor
//@Controller
//public class UserController_bk {
//
//    final UserService userService;
//    final UserRepository userRepository;
//
//
//
//
//
//
//    //마이페이지 조회
//    @GetMapping("/member/mypage")
//   public String showDashboard(Model model) {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    // 현재 사용자의 세션 정보
//    model.addAttribute("userSession", authentication.getPrincipal());
//    return "member/mypage";
//}
//
//    //페이징+검색
//@GetMapping("/")
//public String userlist(Model model, @PageableDefault(size = 5) Pageable pageable) {
//
//    // 로그인 계정
//   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    model.addAttribute("userSession", authentication.getPrincipal());
//    System.out.println("세션에 뭐있나??"+authentication.getPrincipal());
//// 정렬 조건: "createdDate" 필드를 기준으로 내림차순 정렬
//    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
//    // 페이징된 전체 회원 목록
//    Page<User> users = userRepository.findAll(pageable);
//    int currentPage = users.getPageable().getPageNumber() + 1;
//    int startPage = Math.max(1, currentPage - 2);
//    int endPage = Math.min(users.getTotalPages(), startPage + 4);
//    model.addAttribute("startPage", startPage);
//    model.addAttribute("endPage", endPage);
//    model.addAttribute("user", users);
//
//    // 무작위 회원 3명
//    List<User> randomUsers = userRepository.findRandomUsers();
//    model.addAttribute("randomUser", randomUsers);
//
//    return "index";
//}
//
//
//
//@GetMapping("/user/userdetail")
//public String getUserDetail(@RequestParam(required = false) String id, Model model) {
//    // 사용자 정보를 id를 기반으로 데이터베이스에서 가져오는 코드 작성
//    User userdetail = userRepository.findByUserId(id);
//    if (userdetail == null) {
//        // 사용자 정보가 없을 경우 적절한 처리
//        return "redirect:/error"; // 예를 들어, 오류 페이지로 리다이렉트
//    }
//    model.addAttribute("user", userdetail);
//    //로그인 회원 세션 활용
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    // 현재 사용자의 세션 정보
//    model.addAttribute("userSession", authentication.getPrincipal());
//    return "member/userdetail"; // 사용자 정보가 있는 경우 상세 정보 페이지로 이동
//}
//
//
//@GetMapping("user/searchbyname")
//public String getUserDetailbySearch(@RequestParam(required = false) String userId, Model model) {
//    // 사용자 정보를 userId를 기반으로 데이터베이스에서 가져오는 코드 작성
//    if (userId == null || userId.isEmpty()) {
//        // userId가 비어있거나 null인 경우 적절한 처리
//        return "redirect:/";
//    }
//
//    User userdetail = userRepository.findByUserId(userId);
//
//    if (userdetail == null) {
//        // 사용자 정보가 없을 경우 적절한 처리
//        return "redirect:/";
//    }
//
//    model.addAttribute("user", userdetail);
//    // 로그인 회원 세션 활용
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    // 현재 사용자의 세션 정보
//    model.addAttribute("userSession", authentication.getPrincipal());
//    return "member/userdetail"; // 사용자 정보가 있는 경우 상세 정보 페이지로 이동
//}
//
//
//
//        //회원가입
//        @PostMapping("/user/save")
//    public String JoinTest(@ModelAttribute("user") User user, Model model, @PageableDefault(size = 3) Pageable pageable) {
//        Page<User> users = userRepository.findAll(pageable);
//        int currentPage = users.getPageable().getPageNumber() + 1; // 현재 페이지 번호 (0부터 시작)
//        int startPage = Math.max(1, currentPage - 2); // 현재 페이지 주변에 2 페이지씩 보여주기
//        int endPage = Math.min(users.getTotalPages(), startPage + 4);
//        model.addAttribute("startPage",startPage);
//        model.addAttribute("endPage",endPage);
//        model.addAttribute("user", users);
//        System.out.println("회원가입되나");
//        userService.save(user);
//        System.out.println("회원가입안되나");
//            return "redirect:/";
//        }
//}
