//package com.eek.kimpli.voice;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@SpringBootApplication
//public class SignalingServerApplication {
//
////    public static void main(String[] args) {
////        SpringApplication.run(SignalingServerApplication.class, args);
////    }
////
////    @Configuration
////    @EnableWebSocketMessageBroker
////    public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
////
////        @Override
////        public void configureMessageBroker(MessageBrokerRegistry config) {
////            config.enableSimpleBroker("/topic");
////            config.setApplicationDestinationPrefixes("/app");
////        }
////
////        @Override
////        public void registerStompEndpoints(StompEndpointRegistry registry) {
////            System.out.println("test@@@");
////            registry.addEndpoint("/voice").setAllowedOrigins("*").withSockJS();
////        }
////    }
//
//    @Controller
//    @CrossOrigin(origins = "*")
//    public class SignalingController {
//
//        private final SimpMessagingTemplate messagingTemplate;
//
//        public SignalingController(SimpMessagingTemplate messagingTemplate) {
//            this.messagingTemplate = messagingTemplate;
//        }
//
//        @MessageMapping("/offer")
//        public void offer(String offer) {
//            System.out.println("@@@!!!@@@");
//            messagingTemplate.convertAndSend("/topic/offer", offer);
//        }
//
//        @MessageMapping("/answer")
//        public void answer(String answer) {
//            messagingTemplate.convertAndSend("/topic/answer", answer);
//        }
//
//        @MessageMapping("/iceCandidate")
//        public void iceCandidate(String candidate) {
//            messagingTemplate.convertAndSend("/topic/iceCandidate", candidate);
//        }
//    }
//}
