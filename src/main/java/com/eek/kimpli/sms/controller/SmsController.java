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
import com.eek.kimpli.user.model.User;
import com.eek.kimpli.user.repository.UserRepository;
import com.eek.kimpli.user.service.UserService;
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
    private final UserRepository userRepository;
    private final UserService userService;

    //주소로 POST 요청
    @PostMapping("/sms/send")
    @ResponseBody
    public HashMap<String, Object> sendSms(MessageDTO messageDto, HttpSession session) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println("폰넘버?"+messageDto.getTo());
        //삭제 안된 회원의 번호만 찾기 가능
//        User user=userService.findByPhoneNumber(messageDto.getTo());
//        user.getUserId(); //아이디
//        messageDto.getTo(); //휴대폰번호


        HashMap<String, Object> smsMap = new HashMap<>();
        SmsResponseDTO response = smsService.sendSms(messageDto);
        String phoneNumber = messageDto.getTo();
//난수
        int num = smsService.getRandomNumber();
        //서버로 넘어온 회원의 전화번호
        session.setAttribute("phoneNumber", phoneNumber);
        session.setAttribute("num", num); // 세션에 num 값을 저장
        smsMap.put("num", num);
        smsMap.put("response", response);

        System.out.println("결과 뭔데"+smsMap);
        return smsMap;
    }

    //아디찾기, 회원정보 업데이트,회원탈퇴
    @PostMapping("/compareCodes")
    @ResponseBody
    public Map<String, String> compareCodes(@RequestParam String userInputNumber, @RequestParam String inputedPhonenumber, @RequestParam(required = false) String userId, HttpSession session) {

        Map<String, String> resultMap = new HashMap<>();

        // 서버에서 생성한 코드 가져오기
        Integer serverGeneratedCode = (Integer) session.getAttribute("num");
        // 세션에 있던 회원의 전화번호
        String userPhoneNum = (String) session.getAttribute("phoneNumber");

        if (userId == null) {
            // 사용자가 입력한 코드와 서버에서 생성한 코드 비교
            if (userInputNumber.equals(String.valueOf(serverGeneratedCode)) && inputedPhonenumber.equals(userPhoneNum)) {
                // 전화번호 비교
                resultMap.put("result", "success");
            } else {
                resultMap.put("result", "failure");
            }
        } else {
            User userdetail = userRepository.findByUserId(userId);
            if (userId != null && userInputNumber.equals(String.valueOf(serverGeneratedCode)) && userdetail.getPhoneNumber().equals(inputedPhonenumber)) {
                resultMap.put("result", "success");
                resultMap.put("result", "success");
            } else {
                resultMap.put("result", "failure");
            }
        }

        return resultMap;
    }

}