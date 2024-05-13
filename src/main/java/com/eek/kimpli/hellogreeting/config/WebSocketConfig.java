package com.eek.kimpli.hellogreeting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Lazy //순환참조 방지
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        //클라이언트로 보냄
//        config.enableSimpleBroker("/topic");
//        //서버로 보냄
//        config.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/gs-guide-websocket");
//    }

    // 사용자 로그아웃 시 웹 소켓 이벤트 발송
    public void sendUserLogoutEvent(String username) {
        // 주제를 구독하는 클라이언트들에게 메시지가 전달됨.
        messagingTemplate.convertAndSend("/topic/userLogout", username);
    }

       @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
            config.enableSimpleBroker("/topic");
            config.setApplicationDestinationPrefixes("/app");
        }

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            System.out.println("test@@@");
            registry.addEndpoint("/voice");
        }



}