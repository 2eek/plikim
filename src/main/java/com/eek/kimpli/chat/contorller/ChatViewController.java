package com.eek.kimpli.chat.contorller;


import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.repository.ChatRepository;
import com.eek.kimpli.chat.service.ChatUpdater;
import com.eek.kimpli.encryptionUtils.AesUtil;
import com.eek.kimpli.user.model.User;
import com.eek.kimpli.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ChatViewController {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final ChatUpdater chatUpdater;

    @Value("${spring.data.mongodb.uri}")
      private String uri;

    @GetMapping("/mychatrooms")
    public String chatRoomList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName(); // 기본적으로는 authentication에서 가져오기

//        if (userName == null || userName.isEmpty()) {
//            Object principal = authentication.getPrincipal();
//            if (principal instanceof Map) {
//                // principal이 Map인 경우, Map에서 username을 추출하여 userName으로 사용
//                userName = ((Map<String, String>) principal).get("username");
//                model.addAttribute("userSession", userName);
//                System.out.println("사용자 아이디: " + userName);
//            } else {
//                System.out.println("UserDetails가 아닌 Principal입니다: " + principal);
//            }
//        }

        final String finalUserName = userName; // final 또는 effectively final로 선언

        Flux<Chat> chatRoomFlux = chatRepository.findAll();
        List<Chat> chatRoomList = chatRoomFlux.collectList().block();

        if (chatRoomList != null) {
            Set<String> uniqueRoomNums = new HashSet<>();

            chatRoomList = chatRoomList.stream()
                    .filter(chat -> chat.getRoomNum() != null && chat.getRoomNum().contains(finalUserName))
                    .sorted(Comparator.comparing(Chat::getCreatedAt).reversed())
                    .filter(chat -> uniqueRoomNums.add(chat.getRoomNum()))
                    .map(chat -> {
                        try {
                            // 메시지 복호화
                            String decryptedMessage = AesUtil.aesCBCDecode(chat.getMsg());
                            chat.setMsg(decryptedMessage);
                        } catch (Exception e) {
                            e.printStackTrace(); // 복호화 실패 시 예외 처리
                        }
                        return chat;
                    })
                    .collect(Collectors.toList());
        }

        model.addAttribute("userSession", userName);
        model.addAttribute("chatList", chatRoomList);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy년 MM월 dd일 HH:mm");
        for (Chat chat : chatRoomList) {
            LocalDateTime createdAt = chat.getCreatedAt();
            String formattedDate = createdAt.format(formatter);
            chat.setFormattedCreatedAt(formattedDate);
        }
        System.out.println("이름: " + userName);
//Chat 객체의 리스트를 순회하면서 각 Chat 객체에서 roomNum 값을 가져와서 roomNumList라는 List<String>에 추가
        List<String> roomNumList = new ArrayList<>();
        for (Chat chat : chatRoomList) {
            String roomNum = chat.getRoomNum();
            String replacedReceiver = chat.getRoomNum().replaceFirst(userName, "").trim();
            chat.setReceiver(replacedReceiver);
            System.out.println("roomNum: " + roomNum);
            roomNumList.add(roomNum);
        }
        model.addAttribute("roomNumList", roomNumList);
        System.out.println("리스트에 뭐있나" + roomNumList);

        return "chat/mychatrooms";
    }

    // 중복된 userName을 제거한 roomNum을 반환하는 메서드
//    private String removeUserName(String roomNum, String userName) {
//        return roomNum.replace(userName, "");
//    }

    @GetMapping("/chat")
    public String chat(Model model,
                       @RequestParam(name = "userId") String userId,
                       @RequestParam(name = "roomNum", required = false) String roomNum) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userDetails.getUsername();
        System.out.println("나의 아이디 " + userDetails.getUsername());
        System.out.println("방번호" + roomNum);
        String username = userDetails.getUsername();
        ChatUpdater chatUpdater = new ChatUpdater();
        chatUpdater.markRead(username, roomNum, uri);
        model.addAttribute("userId", userId);
        User userById = userService.getUserById(userId);
        model.addAttribute("userinfo", userById);

        model.addAttribute("userSession", authentication.getPrincipal());
        model.addAttribute("roomNum", roomNum); // roomNum을 모델에 추가
        System.out.println("채팅 시 로그인한 계정: " + authentication.getPrincipal());
        return "chat/chat";
    }
}