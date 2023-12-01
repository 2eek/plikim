//package com.eek.kimpli.controller;
//
//import com.eek.kimpli.chat.model.Chat;
//import com.eek.kimpli.chat.repository.ChatRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import reactor.core.publisher.Flux;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Controller
//@RequiredArgsConstructor
//public class HomeController_bk {
//    private final ChatRepository chatRepository;
//
//    @GetMapping("/mychatrooms")
//    public String chatRoomList(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getName();
//        System.out.println("세션에 담긴 회원 이름: " + userName);
//
//        Flux<Chat> chatRoomFlux = chatRepository.findAll();
//        List<Chat> chatRoomList = chatRoomFlux.collectList()
//                .block();
//
//        if (chatRoomList != null) {
//            Set<String> uniqueRoomNums = new HashSet<>();
//
//            chatRoomList = chatRoomList.stream()
//                    .filter(chat -> chat.getRoomNum() != null && chat.getRoomNum().contains(userName))
//                    .sorted(Comparator.comparing(Chat::getCreatedAt).reversed())
//                    .filter(chat -> uniqueRoomNums.add(chat.getRoomNum()))
//                    .collect(Collectors.toList());
//        }
//
//        model.addAttribute("userSession", userName);
//        model.addAttribute("chatList", chatRoomList);
//        System.out.println("이름: " + userName);
//
//        //System.out.println(chatRoomList);
//        List<String> roomNumList = new ArrayList<>();
//        for (Chat chat : chatRoomList) {
//            String roomNum = chat.getRoomNum();
//            System.out.println("roomNum: " + roomNum);
//            roomNumList.add(roomNum);
//        }
//        model.addAttribute("roomNumList", roomNumList);
//
//
//        return "chat/mychatrooms";
//    }
//
//    // 중복된 userName을 제거한 roomNum을 반환하는 메서드
//    private String removeUserName(String roomNum, String userName) {
//        return roomNum.replace(userName, "");
//    }
//}
//
