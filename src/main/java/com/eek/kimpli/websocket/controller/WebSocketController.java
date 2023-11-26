package com.eek.kimpli.websocket.controller;

import com.eek.kimpli.websocket.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class WebSocketController {

    @Autowired
    private UserStatusService userStatusService;

    @MessageMapping("/user-logged-in")
    public void userLoggedIn(Principal principal) {
        userStatusService.userLoggedIn(principal);
    }

    @MessageMapping("/user-logged-out")
    public void userLoggedOut(Principal principal) {
        userStatusService.userLoggedOut(principal);
    }

    @MessageMapping("/get-online-users")
    @SendTo("/topic/online-users")
    public List<String> getOnlineUsers(SimpMessageHeaderAccessor headerAccessor) {
        // 이 메서드에서 특별한 동작이 필요하다면 추가적으로 처리할 수 있습니다.
        return userStatusService.getOnlineUsers();
    }
}

