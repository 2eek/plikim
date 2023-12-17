package com.eek.kimpli.kakaoLogin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoComponent {

    @Value("${kakao.client-id}")
    private String kakaoClientId;
    @Value("https://plikim.com/kakaologin")
//    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.response-type}")
    private String kakaoResponseType;

    public String getKakaoClientId() {
        return kakaoClientId;
    }

    public void setKakaoClientId(String kakaoClientId) {
        this.kakaoClientId = kakaoClientId;
    }

    public String getKakaoRedirectUri() {
        return kakaoRedirectUri;
    }

    public void setKakaoRedirectUri(String kakaoRedirectUri) {
        this.kakaoRedirectUri = kakaoRedirectUri;
    }

    public String getKakaoResponseType() {
        return kakaoResponseType;
    }

    public void setKakaoResponseType(String kakaoResponseType) {
        this.kakaoResponseType = kakaoResponseType;
    }
}
