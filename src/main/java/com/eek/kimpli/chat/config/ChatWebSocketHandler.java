//package com.eek.kimpli.chat.config;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Controller
//public class ChatWebSocketHandler {
//
//    private final SimpMessagingTemplate messagingTemplate;
//    private final Map<String, Boolean> userStatusMap;
//
//    public ChatWebSocketHandler(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//        this.userStatusMap = new ConcurrentHashMap<>();
//    }
//
//    @MessageMapping("/updateStatus")
//    public void updateStatus(@Payload StatusUpdate statusUpdate, SimpMessageHeaderAccessor headerAccessor) {
//        String username = headerAccessor.getUser().getName();
//
//        // 사용자 상태 업데이트
//        userStatusMap.put(username, statusUpdate.isOnline());
//
//        // 읽음 상태 업데이트
//        if (statusUpdate.isRead()) {
//            // 채팅을 읽었다는 처리
//            // 여기에서 채팅 읽음 상태를 업데이트하고, 필요한 로직 수행
//        }
//
//        // 다른 사용자에게 상태를 알림
//        messagingTemplate.convertAndSend("/topic/userStatus", userStatusMap);
//    }
//}
