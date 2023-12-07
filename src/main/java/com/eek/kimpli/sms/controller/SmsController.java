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

    int num = smsService.getRandomNumber();

    session.setAttribute("num", num); // 세션에 num 값을 저장
    smsMap.put("num", num);
    smsMap.put("response", response);
    System.out.println("여기 뭐있나 아이디 찾기 결과" + smsMap);

    return smsMap;
}

@PostMapping("/compareCodes")
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