package com.eek.kimpli.member.service;

import com.eek.kimpli.member.dto.MemberDTO;

import java.util.Map;

public interface KakaoLoginService {

public	Map<String, String> getAccessToken (String authorize_code);
public MemberDTO getUserInfo(String access_Token, String refresh_Token) ;

}