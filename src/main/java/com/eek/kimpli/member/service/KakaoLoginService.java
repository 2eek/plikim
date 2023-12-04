package com.eek.kimpli.member.service;

import com.eek.kimpli.user.model.User;

import java.util.Map;

public interface KakaoLoginService {

    Map<String, String> getAccessToken(String authorize_code);

    User getUserInfo(String access_Token, String refresh_Token);
}
