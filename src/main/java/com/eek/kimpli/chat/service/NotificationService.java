package com.eek.kimpli.chat.service;

import com.eek.kimpli.chat.model.Notification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(String username) {
        // 해당 사용자에게 알림을 전송
        messagingTemplate.convertAndSendToUser(username, "/queue/notification",
                new Notification("You have a new chat message!"));
    }
}
