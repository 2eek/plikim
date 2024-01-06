package com.eek.kimpli.hellogreeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Lazy //순환참조 방지
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {registry.addEndpoint("/gs-guide-websocket");
  }
   // 사용자 로그아웃 시 웹 소켓 이벤트 발송

    public void sendUserLogoutEvent(String username) {
        // 주제를 구독하는 클라이언트들에게 메시지가 전달됨.
        messagingTemplate.convertAndSend("/topic/userLogout", username);
    }


}