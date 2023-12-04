package com.eek.kimpli.member.service;

import com.eek.kimpli.member.dto.MemberDTO;

import java.util.Map;

public interface KakaoLoginService {

public	Map<String, String> getAccessToken (String authorize_code); // 매개변수로 인가 코드를 받아오고, Map<String, String> 형태로 액세스 토큰과 리프레시 토큰을 반환
public MemberDTO getUserInfo(String access_Token, String refresh_Token) ;//   액세스 토큰과 리프레시 토큰을 받아오고, MemberDTO라는 데이터 전송 객체를 반환
}