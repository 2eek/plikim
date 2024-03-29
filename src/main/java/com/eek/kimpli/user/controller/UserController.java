package com.eek.kimpli.user.controller;

import com.eek.kimpli.user.model.User;
import com.eek.kimpli.user.repository.UserRepository;
import com.eek.kimpli.user.service.UserService;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //가입한 회원 리스트 조회. 단순한 호출 GetMapping을 이용한다. URL에 매핑된 핸들러 필요하다
    @GetMapping("/user/memberlist")
    public String saveForm() {
        return "user/memberlist";
    }

    //회원가입폼 호출
    @GetMapping("/user/memberjoin")
    public String joinForm() {
        return "user/memberjoinform";
    }

    //로그인폼
    @GetMapping("/user/loginForm")
    public String loginForm() {
        return "user/login";
    }

    //마이페이지
    @GetMapping("/user/mypage")
    public String showMyPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Spring Security의 UserDetails 정보 활용
        // 이때, UserDetails에는 사용자의 아이디, 패스워드, 권한 등의 기본 정보만 들어있다

        // 여기서 사용자 모델(User 클래스)에 추가된 정보를 가져와서 모델에 추가
        User customUser = userService.getUserById(userDetails.getUsername());
        model.addAttribute("userId", customUser.getUserId());
        model.addAttribute("userName", customUser.getUserName());
        model.addAttribute("userEmail", customUser.getEmail());
        model.addAttribute("createdDate", customUser.getCreatedDate());
        model.addAttribute("userPhoneNumber", customUser.getPhoneNumber());
        model.addAttribute("userLoginType", customUser.getLoginType());

        return "user/mypage";
    }

    //페이징+검색
    @GetMapping("/")
    public String userlist(Model model, @PageableDefault(size = 5) Pageable pageable) {

        // 로그인 계정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("userSession", authentication.getPrincipal());
        // 정렬 조건: "createdDate" 필드를 기준으로 내림차순 정렬
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        // 페이징된 전체 회원 목록
        //  Page<User> users = userRepository.findAll(pageable);
        Page<User> users = userRepository.findUndeletedUser(pageable);
        int currentPage = users.getPageable().getPageNumber() + 1;
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(users.getTotalPages(), startPage + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("user", users);

        // 무작위 회원 3명
        List<User> randomUsers = userRepository.findRandomUsers();
        model.addAttribute("randomUser", randomUsers);

        return "index";
    }


    @GetMapping("/user/userdetail")
    public String getUserDetail(@RequestParam(required = false) String id, Model model) {
        // 사용자 정보를 id를 기반으로 데이터베이스에서 가져오는 코드 작성
        User userdetail = userRepository.findByUserId(id);
        if (userdetail == null) {
            // 사용자 정보가 없을 경우 적절한 처리
            return "redirect:/"; // 예를 들어, 오류 페이지로 리다이렉트
        }
        model.addAttribute("user", userdetail);
        //로그인 회원 세션 활용
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 사용자의 세션 정보
        model.addAttribute("userSession", authentication.getPrincipal());
        return "user/userdetail"; // 사용자 정보가 있는 경우 상세 정보 페이지로 이동
    }


    @GetMapping("user/searchbyname")
    public String getUserDetailbySearch(@RequestParam(required = false) String userId, Model model) {
        // 사용자 정보를 userId를 기반으로 데이터베이스에서 가져오는 코드 작성
        if (userId == null || userId.isEmpty()) {
            // userId가 비어있거나 null인 경우 적절한 처리
            return "redirect:/";
        }

        User userdetail = userRepository.findByUserIdAndDeleted(userId, 0);

        if (userdetail == null) {
            // 사용자 정보가 없을 경우 적절한 처리
            return "redirect:/";
        }

        model.addAttribute("user", userdetail);
        // 로그인 회원 세션 활용
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 사용자의 세션 정보
        model.addAttribute("userSession", authentication.getPrincipal());
        return "user/userdetail"; // 사용자 정보가 있는 경우 상세 정보 페이지로 이동
    }


    //회원가입
    @PostMapping("/user/save")
    @ResponseBody
    public int Join(@ModelAttribute("user") User user, Model model, @PageableDefault(size = 3) Pageable pageable) {

        return userService.save(user);

    }

    //회원 프로필 사진 변경
    @ResponseBody
    @PostMapping("/user/updateProfile")
    public int updateProfile(@RequestParam("profileFile") MultipartFile profileFile, String userId) {
        try {
            // 프로필 사진 업데이트
//            System.out.println("아디 프사 확인" + userId + profileFile);
            return userService.updateProfile(userId, profileFile);
        } catch (IOException e) {
            // IOException 처리
            e.printStackTrace(); // 또는 로깅 등으로 예외 처리
            return -1; // 예외 발생 시 반환할 값
        }
    }


    @Transactional
    @ResponseBody
    @PostMapping("/updatePhoneNumber")
    public int updatePhoneNumber(String phoneNumber, String userId) {
        return userService.updatePhoneNumber(phoneNumber, userId);
    }

    @Transactional
    @ResponseBody
    @PostMapping("/updateEmail")
    public int updateEmail(String email, String userId) {
        return userService.updateEmail(email, userId);
    }


    //회원가입시 휴대폰 중복 검사 (where= deleted 0) 추가완료
    @ResponseBody
    @PostMapping("/phoneNumberCheck")
    public User phoneNumberCheck(String phoneNumber) {
        User user = userService.findByPhoneNumber(phoneNumber);

        if (user != null) {
            //db에 있는 휴대폰 번호다
            return user;
        } else {
            return new User(); // 빈 User 객체 또는 다른 적절한 값을 반환

        }
    }

    @ResponseBody
    @PostMapping("/emailAndIdCheck")
    public User checkEmailAndUserId(String email, String id) {
        User user = userService.checkEmailAndUserId(email, id);
        if (user != null) {
            return user;
        } else {
            return new User(); // 빈 User 객체 또는 다른 적절한 값을 반환

        }
    }


    @PostMapping("/emailCheck")
    @ResponseBody
    public int emailCheck(@RequestParam String email) {
        int emailFormatResult = isValidEmailFormat(email);

        if (emailFormatResult == 0) { //이메일 형식이라면 0 반환됨
            return 0;
        } else {
            // 이메일 형식 아니면 -1 반환 -> 서버로 null값 반환한다.
            return -1;
        }
    }

    private int isValidEmailFormat(String email) {

        //빈 값 체크
        if (email == null || email.isEmpty()) {
            return -1;
        }
        // 이메일 형식 검사(한글x 숫자 알파벳만 가능)
        String emailRegex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$";
        return email.matches(emailRegex) ? 0 : -1;
    }


    //휴대폰 번호로 아이디 찾기
    @GetMapping("/user/findAccount")
    public String fintId() {
        return "user/findAccount";
    }

    //이메일로 인증 받기
    @GetMapping("/user/findPassword")
    public String findPassword() {
        return "user/findPassword";
    }

    //비밀번호 찾기. 이메일 인증 후  비밀번호 업데이트 폼 호출
    @PostMapping("/updatePasswordForm")
    public String updatePassword(String userId, String email, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("email", email);
        return "user/updatePassword";
    }


    @PostMapping("/editPassword")
    public int EditPassword(String password, String userId) {
        return userService.updatePassword(userId, password);
    }

    //비밀번호찾기, myPage 비밀번호 업데이트
    @PostMapping("/updatePassword")
    @ResponseBody
    public int updatePassword(String userId, String password) {
        return userService.updatePassword(userId, password);
    }

    //회원 가입시 아이디 중복체크
    @PostMapping("/idCheck")
    @ResponseBody
    public int idCheck(@RequestParam("userId") String id) {
        // 아이디에 공백이나 한글이 있는지 정규 표현식으로 검사
        if (id.length() < 5 || id.matches(".*\\s.*") || !id.matches("^[a-zA-Z0-9]*$")) {
            return 2; // 2는 공백이나 한글이 있는 경우를 나타냄
        }

        User result = userService.checkId(id);

        if (result == null) {
            return 0; // 사용 가능한 아이디
        } else {
            return 1; // 이미 존재하는 아이디
        }
    }

    @GetMapping("/user/withdrawForm")
    public String withdrawForm() {

        return "user/withdrawForm"; // 사용자 정보가 있는 경우 상세 정보 페이지로 이동
    }

    @PostMapping("/user/withdraw")
    @ResponseBody
    public int withdraw(String phoneNumber, String userId) {
        System.out.println("폰넘버, 유저아이디 " + phoneNumber + userId);
        return userService.withdraw(phoneNumber, userId);
    }

    //비밀번호 업데이트용 현재 비밀번호 체크
    @PostMapping("/currentPwCheck")
    @ResponseBody
    public int currentPwCheck(String currentPw, String userId) {
        User user = userService.getUserById(userId);
        if (currentPw == null || currentPw.isEmpty()) {
            return 0;
        } else {
            if (passwordEncoder.matches(currentPw, user.getPassword())) {
                return 1; // 비번이 같다
            } else {
                return -1; // 비번이 다르다
            }
        }
    }
}