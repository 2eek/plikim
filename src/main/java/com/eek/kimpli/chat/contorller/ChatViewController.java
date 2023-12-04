package com.eek.kimpli.chat.contorller;

import com.eek.kimpli.EncryptionUtils.AesUtil;
import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ChatViewController {

     private final ChatRepository chatRepository;

    @GetMapping("/mychatrooms")
    public String chatRoomList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        System.out.println("세션에 담긴 회원 이름: " + userName);

        Flux<Chat> chatRoomFlux = chatRepository.findAll();
        List<Chat> chatRoomList = chatRoomFlux.collectList().block();

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
        System.out.println("이름: " + userName);

        List<String> roomNumList = new ArrayList<>();
        for (Chat chat : chatRoomList) {
            String roomNum = chat.getRoomNum();
            System.out.println("roomNum: " + roomNum);
            roomNumList.add(roomNum);
        }
        model.addAttribute("roomNumList", roomNumList);

        return "chat/mychatrooms";
    }

    // 중복된 userName을 제거한 roomNum을 반환하는 메서드
    private String removeUserName(String roomNum, String userName) {
        return roomNum.replace(userName, "");
    }

    @GetMapping("/chat")
    public String chat(Model model, @RequestParam(name = "userId") String userId) {
        model.addAttribute("userId", userId); // userId를 모델에 추가' 대화 상대방.
        //로그인한 계정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("userSession", authentication.getPrincipal());
        System.out.println("채팅 시 로그인한 계정"+authentication.getPrincipal());
        return "chat/chat";
    }
}
