//package com.eek.kimpli.kakaoLogin.Controller;
//
//import java.util.Map;
//
//import com.eek.kimpli.kakaoLogin.service.KakaoLoginServiceImpl;
//import com.eek.kimpli.user.model.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//@RequiredArgsConstructor//생성자를 자동으로 생성
//public class KakaoLoginController_bk2 {
//    //접근제어자. 변수초기화 후 값 변경x 필드의 타입 (객체 참조 가능) 멤버변수(필드) 선언, 식별자
//   private final KakaoLoginServiceImpl ks;
////   private final HttpSession session;
//    private final User user;
//
//
////사용자가 로그인을 성공적으로 수행한 후에 리디렉션될 URL
//   @GetMapping("/kakaologin")
//   public String kakaoLogin(@RequestParam(value = "code", required = false) String code, Model model) throws Exception {
//
//
////       System.setProperty("file.encoding", "UTF-8"); ?? 한글 안되나?
//       //접근 토큰을 받아옴
//       //데이터를 저장하는 자료구조 Map<키,밸류>
//        //액세스토큰 얻어오기
//      Map<String, String> map = ks.getAccessToken(code);
//      String access_Token = map.get("access_token");
//      String refresh_Token = map.get("refresh_token");
//
//
//       System.out.println("aaaaaaaaaaaa");
//        User user = ks.getUserInfo(access_Token, refresh_Token);
//       System.out.println("bbbbbbbbbb");
//      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    model.addAttribute("userSession", authentication.getPrincipal());
//    System.out.println("프린시펄 일반로그인"+authentication.getPrincipal());
//
////                   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////    model.addAttribute("userSession", authentication.getPrincipal());
////       String userName = authentication.getName();
////       System.out.println("유저네임?? 카카오"+userName);
////    System.out.println("모델??"+model);
////    System.out.println("유저세션"+authentication);
////    System.out.println("프린시펄 카카오로그인 "+authentication.getPrincipal());
// //getUserInfo 메서드 호출해서 시행함.
//      return "redirect:/";
//       }
//
//}
//
