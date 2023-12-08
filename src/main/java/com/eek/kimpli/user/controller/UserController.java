package com.eek.kimpli.user.controller;

import com.eek.kimpli.user.model.User;
import com.eek.kimpli.user.repository.UserRepository;
import com.eek.kimpli.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class UserController {

    final UserService userService;
    final UserRepository userRepository;




    //가입한 회원 리스트 조회. 단순한 호출 GetMapping을 이용한다. URL에 매핑된 핸들러 필요하다
    @GetMapping("/member/memberlist")
    public String saveForm(){
        return "member/memberlist";
    }

    //회원가입폼 호춯
    @GetMapping("/member/memberjoin")
    public String joinForm(){
        return "member/memberjoinform";
    }

    @GetMapping("/member/loginForm")
    public String loginForm(){
        return "member/login";
    }



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
public String userlist(Model model, @PageableDefault(size = 5) Pageable pageable) {

    // 로그인 계정
   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("userSession", authentication.getPrincipal());
    System.out.println("프린시펄 일반로그인"+authentication.getPrincipal());
// 정렬 조건: "createdDate" 필드를 기준으로 내림차순 정렬
    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
    // 페이징된 전체 회원 목록
    Page<User> users = userRepository.findAll(pageable);
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
//    System.out.println("유저디테일"+userdetail);
    System.out.println("프린시펄 "+authentication.getPrincipal());
    return "member/userdetail"; // 사용자 정보가 있는 경우 상세 정보 페이지로 이동
}


@GetMapping("user/searchbyname")
public String getUserDetailbySearch(@RequestParam(required = false) String userId, Model model) {
    // 사용자 정보를 userId를 기반으로 데이터베이스에서 가져오는 코드 작성
    if (userId == null || userId.isEmpty()) {
        // userId가 비어있거나 null인 경우 적절한 처리
        return "redirect:/";
    }

    User userdetail = userRepository.findByUserId(userId);

    if (userdetail == null) {
        // 사용자 정보가 없을 경우 적절한 처리
        return "redirect:/";
    }

    model.addAttribute("user", userdetail);
    // 로그인 회원 세션 활용
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // 현재 사용자의 세션 정보
    model.addAttribute("userSession", authentication.getPrincipal());
    return "member/userdetail"; // 사용자 정보가 있는 경우 상세 정보 페이지로 이동
}



        //회원가입
        @PostMapping("/user/save")
    public String Join(@ModelAttribute("user") User user, Model model, @PageableDefault(size = 3) Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
           // 아이디 중복 체크
//  if (userRepository.existsByUserId(user.getUserId())) {
//    // 아이디가 이미 존재하는 경우 처리 (예: 에러 메시지 전달)
//    model.addAttribute("error", "아이디가 이미 존재합니다.");
//        return "redirect:/user/save"; // 에러를 보여줄 뷰로 리다이렉트 또는 포워드
//    }
//
//    // 이메일 중복 체크
//    if (userRepository.existsByEmail(user.getEmail())) {
//        // 이메일이 이미 존재하는 경우 처리 (예: 에러 메시지 전달)
//        model.addAttribute("error", "이미 사용 중인 이메일입니다.");
//        return "redirect:/user/save"; // 에러를 보여줄 뷰로 리다이렉트 또는 포워드
//    }
//
//    // 휴대폰 번호 중복 체크
//    if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
//        // 휴대폰 번호가 이미 존재하는 경우 처리 (예: 에러 메시지 전달)
//        model.addAttribute("error", "이미 사용 중인 휴대폰 번호입니다.");
//        return "redirect:/user/save"; // 에러를 보여줄 뷰로 리다이렉트 또는 포워드
//    }
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


//번호로 id 찾기
//   @ResponseBody
//@PostMapping("/phoneNumberCheck")
//public ResponseEntity<User> phoneNumberCheck(String phoneNumber) {
//    User user = userService.findByPhoneNumber(phoneNumber);
//
//    if (user != null) {
//        System.out.println("User found: " + user);
//        return ResponseEntity.ok(user);
//    } else {
//        System.out.println("컨트롤 널입니다 " + user);
//        // 사용자가 없을 때도 200 OK를 반환하고 null을 전송
//        return ResponseEntity.ok(null);
//    }
//}

    //이거 되는거임
    @ResponseBody
@PostMapping("/phoneNumberCheck")
public User phoneNumberCheck(String phoneNumber) {
    User user = userService.findByPhoneNumber(phoneNumber);

    if (user != null) {
        System.out.println("User found: " + user);
        return user;
    } else {
//        System.out.println("컨트롤 널입니다" + user);
        return new User(); // 빈 User 객체 또는 다른 적절한 값을 반환

    }
}

    @ResponseBody
@PostMapping("/emailCheck")
public User emailCheck(String email) {
    User user = userService.checkEmail(email);

    if (user != null) {
        System.out.println("User found: " + user);
        return user;
    } else {
//        System.out.println("컨트롤 널입니다" + user);
        return new User(); // 빈 User 객체 또는 다른 적절한 값을 반환

    }
}
//     @PostMapping("/phoneNumberCheck")
//    public ResponseEntity<User> phoneNumberCheck(@RequestParam String phoneNumber) {
//        User user = userService.findByPhoneNumber(phoneNumber);
//
//        if (user != null) {
//            return ResponseEntity.ok(user);
//        } else {
//            System.out.println("뭔데?????????"+ResponseEntity.ok(null));
//            return ResponseEntity.ok(null);
//        }
//    }

//      //입력한 전화번호로 계정 ID 찾기
//		@ResponseBody
//@PostMapping("/phoneNumberCheck")
//public User phoneNumberCheck(@RequestParam String phoneNumber) {
//    User user = userService.findByPhoneNumber(phoneNumber);
//
//    if (user != null) {
//        System.out.println("User found: " + user);
//        return user;
//    } else {
//            System.out.println("dmaskdmaskdmaskdsakdsmoqwmdo: " );
//        // 만약 사용자가 없을 때는 HttpStatus.NOT_FOUND로 응답
//        return null;
//    }
//}

        		//휴대폰 번호로 아이디 찾기
		@GetMapping("/findAccount")
		public String fintId() {
			return"member/findAccount";
		}

        //이메일로 인증 받기
			@GetMapping("/findPassword")
		public String findPassword() {
			return"member/findPassword";
		}

        		//비밀번호 찾기. 이메일 인증 후  비밀번호 업데이트 폼 호출
		@PostMapping("/updatePaswordForm")
		public String updatePassword(User user,Model model) {
			//memberService.insertMemberInfo(memberVO);
		System.out.println(user);
		model.addAttribute("email", user.getEmail());
		System.out.println(user.getEmail());
			return "member/updatePassword";
		}

		//비밀번호 찾기.변경할 비밀번호로 업데이트 시행하기
		@PostMapping("/EditPassword")
		public String EditPassword(User user){
			System.out.println(user);
			userService.editPassword(user);
			/* return "member/login"; */
			return "redirect:/";
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

      //회원 가입시 이메일 중복체크

//     @PostMapping(" /emailCheck")
//	  @ResponseBody
//	  public int emailCheck(String email) {
//          System.out.println("뭐가 들어오냐"+email);
//		User  result = userService.checkEmail(email);
//          System.out.println("리절트"+result);
//		if (result == null) {
//		    return 0;
//		} else {
//		    return 1;
//		}
//
//	  }
//


}