package com.eek.kimpli.hellogreeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YourService {

    private final WebSocketConfig webSocketConfig;

    @Autowired
    public YourService(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
    }

    public void handleUserLogout(String username) {
        // 사용자 로그아웃 처리 로직

        // 웹 소켓 이벤트 발송
        webSocketConfig.sendUserLogoutEvent(username);
    }
}
