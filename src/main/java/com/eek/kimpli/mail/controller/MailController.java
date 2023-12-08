package com.eek.kimpli.mail.controller;

import com.eek.kimpli.mail.Service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/sendemail")
    public String MailPage(){
        return "email/Mail";
    }


  //화면단에서 변수명 mail로 보내왔음, 등록한 전화번호로 난수 보내기
    @ResponseBody
    @PostMapping("/mail")
    public HashMap<String, Object> MailSend(String mail, HttpSession session ){
          int number = mailService.sendMail(mail);
          session.setAttribute("num", number); // 세션에 num 값을 저장
        System.out.println(number);
//           String num = "" + number;
  HashMap<String, Object> emailMap = new HashMap<>();
    emailMap.put("response", mail);


        System.out.println("번호뭔데"+number);
       return emailMap;
    }


@PostMapping("/emailcompareCodes")
@ResponseBody
public Map<String, String> compareCodes(@RequestParam String userInputNumber , HttpSession session) {

      Map<String, String> resultMap = new HashMap<>();

        // 서버에서 생성한 코드 가져오기
        Integer serverGeneratedCode = (Integer) session.getAttribute("num");
		System.out.println("브라우저에서 보내는 코드"+userInputNumber );
		System.out.println("실제 코드"+serverGeneratedCode);


        // 사용자가 입력한 코드와 서버에서 생성한 코드 비교
        if (userInputNumber .equals(String.valueOf(serverGeneratedCode))) {
            resultMap.put("result", "success");
        } else {
            resultMap.put("result", "failure");
        }

        return resultMap;
    }


}
