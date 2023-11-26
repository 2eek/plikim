//package com.eek.kimpli.websocket.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//import java.security.Principal;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//
//@Service
//public class UserStatusService {
//
//    // 서버 측에서 사용자 로그인 상태를 관리하기 위한 맵
//    private Map<String, Boolean> loggedInUsers = new ConcurrentHashMap<>();
//
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//
//    // 사용자 로그인 시 호출
//    public void userLoggedIn(Principal principal) {
//        String username = principal.getName();
//        loggedInUsers.put(username, true);
//        notifyOnlineUsers(); // 로그인 중인 사용자 목록 업데이트
//    }
//
//    // 사용자 로그아웃 시 호출
//    public void userLoggedOut(Principal principal) {
//        String username = principal.getName();
//        loggedInUsers.put(username, false);
//        notifyOnlineUsers(); // 로그인 중인 사용자 목록 업데이트
//    }
//
//    // 현재 로그인 중인 사용자 목록을 동적으로 업데이트하는 핸들러
//    public List<String> getOnlineUsers() {
//        return loggedInUsers.entrySet().stream()
//                .filter(Map.Entry::getValue)
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
//    }
//
//    // 로그인 중인 사용자 목록을 업데이트하는 메서드
//    private void notifyOnlineUsers() {
//        simpMessagingTemplate.convertAndSend("/topic/online-users", getOnlineUsers());
//    }
//}
//
