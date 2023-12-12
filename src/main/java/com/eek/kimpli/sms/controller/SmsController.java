package com.eek.kimpli.sms.controller;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.eek.kimpli.sms.dto.MessageDTO;
import com.eek.kimpli.sms.dto.SmsResponseDTO;
import com.eek.kimpli.sms.service.SmsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class SmsController {

	private final SmsService smsService;


//	   @GetMapping("/send1")
//	   public String getSmsPage() {
//
//		   return"/sms/sendSms";
//	   }


       //주소로 POST 요청
@PostMapping("/sms/send")
@ResponseBody
public HashMap<String, Object> sendSms(MessageDTO messageDto, HttpSession session) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
	System.out.println("회원번호??"+messageDto.getTo());
	HashMap<String, Object> smsMap = new HashMap<>();
    SmsResponseDTO response = smsService.sendSms(messageDto);
    String phoneNumber = messageDto.getTo();

    int num = smsService.getRandomNumber();
    //서버로 넘어온 회원의 전화번호
    session.setAttribute("phoneNumber", phoneNumber);
    session.setAttribute("num", num); // 세션에 num 값을 저장
    smsMap.put("num", num);
    smsMap.put("response", response);
    System.out.println("여기 뭐있나 아이디 찾기 결과" + smsMap);

    return smsMap;
}
@PostMapping("/compareCodes")
@ResponseBody
public Map<String, String> compareCodes(@RequestParam String userInputNumber, @RequestParam String inputedPhonenumber, HttpSession session) {

    Map<String, String> resultMap = new HashMap<>();

    // 서버에서 생성한 코드 가져오기
    Integer serverGeneratedCode = (Integer) session.getAttribute("num");
    // 세션에 있던 회원의 전화번호
    String userPhoneNum = (String) session.getAttribute("phoneNumber");

    System.out.println("브라우저에서 보내는 코드: " + userInputNumber);
    System.out.println("실제 코드: " + serverGeneratedCode);

    // 사용자가 입력한 코드와 서버에서 생성한 코드 비교
    if (userInputNumber.equals(String.valueOf(serverGeneratedCode))) {
        // 전화번호 비교
        if (inputedPhonenumber.equals(userPhoneNum)) {
            //result"라는 키에 "success"라는 값을 매핑
            resultMap.put("result", "success");
        } else {
            resultMap.put("result", "failure");
        }
    } else {
        resultMap.put("result", "failure");
    }

    return resultMap;
}

//@PostMapping("/saveIsCodeConfirmed")
//@ResponseBody
//public Map<String, String> saveIsCodeConfirmed(@RequestParam boolean isCodeConfirmed, HttpSession session) {
//    // 코드 매칭이 맞았다면 세션에  isCodeConfirmed)저장한다. 저장된 값은 true이다.
//     // 코드 매칭이 안맞았다면 세션에  isCodeConfirmed)저장한다. 저장된 값은 false이다.
//    session.setAttribute("isCodeConfirmed", isCodeConfirmed);
//    System.out.println("잉 여기 트루펄스 세션?"+isCodeConfirmed);
//    Map<String, String> resultMap = new HashMap<>();
//    resultMap.put("status", "success");
//    return resultMap;
//}

//@GetMapping("/getIsCodeConfirmed")
//@ResponseBody
//public Map<String, Boolean> getIsCodeConfirmed(HttpSession session) {
//    Boolean isCodeConfirmed = (Boolean) session.getAttribute("isCodeConfirmed");
//    Map<String, Boolean> resultMap = new HashMap<>();
//    System.out.println("true 아니면 false??"+isCodeConfirmed);
//    resultMap.put("isCodeConfirmed", isCodeConfirmed != null && isCodeConfirmed == true);
//    return resultMap;
//}
//
////새로고침시 회원의 코드 검증 기록을 초기화
//// 세션 값을 삭제하는 컨트롤러
//@PostMapping("/clearSessionValue")
//@ResponseBody
//public void clearSessionValue(HttpSession session) {
//    // 세션에서 특정 속성만을 삭제하거나 세션 전체를 초기화할 수 있음
//    session.removeAttribute("isCodeConfirmed");
//}


}