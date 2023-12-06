package com.eek.kimpli.sms.controller;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.eek.kimpli.sms.dto.MessageDTO;
import com.eek.kimpli.sms.dto.SmsResponseDTO;
import com.eek.kimpli.sms.service.SmsService;
import com.eek.kimpli.user.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SmsController {

	private final SmsService smsService;


	   @GetMapping("/send1")
	   public String getSmsPage() {

		   return"/sms/sendSms";
	   }

	   //주소로 POST 요청
	@PostMapping("/sms/send")
	@ResponseBody
		public HashMap<String, Object> sendSms(MessageDTO messageDto, Model model) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
		HashMap<String,Object> smsMap = new HashMap<String,Object>();
		SmsResponseDTO response = smsService.sendSms(messageDto);

		int num = smsService.getRandomNumber();
		model.addAttribute("num", num);
		System.out.println(num);
		model.addAttribute("response", response);
		smsMap.put("num", num);
		smsMap.put("reponse", response);
		System.out.println("여기 뭐있나 아이디 찾기 결과"+smsMap);

		return smsMap;
	}








}