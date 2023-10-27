package com.eek.kimpli.member.Controller;

import java.util.Map;
import javax.servlet.http.HttpSession;
import com.eek.kimpli.member.dto.MemberDTO;
import com.eek.kimpli.member.service.KakaoLoginServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
//@RequestMapping(value="/member")
public class KakaoLoginController {
   private final KakaoLoginServiceImpl ks;
   private final HttpSession session;


//사용자가 로그인을 성공적으로 수행한 후에 리디렉션될 URL
   @GetMapping("/kakaologin")
   public String kakaoLogin(@RequestParam(value = "code", required = false) String code) throws Exception {
       System.setProperty("file.encoding", "UTF-8");
       System.out.println(code);
       //접근 토큰을 받아옴
      Map<String, String> map = ks.getAccessToken(code);
      String access_Token = map.get("access_token");
      String refresh_Token = map.get("refresh_token");
      System.out.println("액세스토큰"+access_Token);
      System.out.println("리프레쉬토큰"+refresh_Token);
       //다시 서비스에 있는 메서드 실행함 그리고 DB에 정보 없으면 insert .xml실행
            MemberDTO memberDTO = ks.getUserInfo(access_Token, refresh_Token);
 //getUserInfo 메서드 호출해서 시행함.
      return "redirect:/";
       }

}
