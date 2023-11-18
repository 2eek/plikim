package com.eek.kimpli.User.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class SessionController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        // 세션 종료 이벤트가 발생할 때 WebSocket을 통해 클라이언트에게 알림
        messagingTemplate.convertAndSend("/topic/session-disconnect", event.getUser().getName());
    }
}
