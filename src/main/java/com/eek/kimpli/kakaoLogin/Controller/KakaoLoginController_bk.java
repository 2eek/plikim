//
//
//package com.eek.kimpli.member.Controller;
//
//import java.util.Map;
//import javax.servlet.http.HttpSession;
//import com.eek.kimpli.member.dto.MemberDTO;
//import com.eek.kimpli.member.service.KakaoLoginServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//@RequiredArgsConstructor//생성자를 자동으로 생성
//public class KakaoLoginController_bk {
//    //접근제어자. 변수초기화 후 값 변경x 필드의 타입 (객체 참조 가능) 멤버변수(필드) 선언, 식별자
//   private final KakaoLoginServiceImpl ks;
////   private final HttpSession session;
//
//
////사용자가 로그인을 성공적으로 수행한 후에 리디렉션될 URL
//   @GetMapping("/kakaologin")
//   public String kakaoLogin(@RequestParam(value = "code", required = false) String code) throws Exception {
////       System.setProperty("file.encoding", "UTF-8"); ?? 한글 안되나?
//       //접근 토큰을 받아옴
//       //데이터를 저장하는 자료구조 Map<키,밸류>
//
//      Map<String, String> map = ks.getAccessToken(code);
//      String access_Token = map.get("access_token");
//      String refresh_Token = map.get("refresh_token");
//       //다시 서비스에 있는 메서드 실행함 그리고 DB에 정보 없으면 insert .xml실행
//            MemberDTO memberDTO = ks.getUserInfo(access_Token, refresh_Token);
// //getUserInfo 메서드 호출해서 시행함.
//      return "redirect:/";
//       }
//
//}
//
