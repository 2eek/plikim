package com.eek.kimpli.chat.contorller;

import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.service.ChatService;
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


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ChatViewController {
private final ChatService chatService;
    private final UserService userService;
    private final ChatUpdater chatUpdater;

    @Value("${spring.data.mongodb.uri}")
      private String uri;

    @GetMapping("/mychatrooms")
    public String chatRoomList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String userName = authentication.getName(); // final 또는 effectively final로 선언
            List<Chat> chatRoomList= chatService.getAllChats();

        if (chatRoomList != null) {
            Set<String> uniqueRoomNums = new HashSet<>();
            chatRoomList = chatRoomList.stream()
                    .filter(chat -> chat.getRoomNum() != null && chat.getRoomNum().contains(userName))
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
        List<String> roomNumList = new ArrayList<>();
        for (Chat chat : chatRoomList) {
            String roomNum = chat.getRoomNum();
            String replacedReceiver = chat.getRoomNum().replaceFirst(userName, "").trim();
            chat.setReceiver(replacedReceiver);
            roomNumList.add(roomNum);
        }
        model.addAttribute("roomNumList", roomNumList);
        return "chat/mychatrooms";
    }

    @GetMapping("/chat")
    public String chat(Model model,
                       @RequestParam(name = "userId") String userId,
                       @RequestParam(name = "roomNum", required = false) String roomNum) {
        ChatUpdater chatUpdater = new ChatUpdater();
        chatUpdater.markRead(userId, roomNum, uri);
        model.addAttribute("userId", userId);
        User userById = userService.getUserById(userId);
        model.addAttribute("userinfo", userById);
        model.addAttribute("roomNum", roomNum); // roomNum을 모델에 추가
        return "chat/chat";
    }
}