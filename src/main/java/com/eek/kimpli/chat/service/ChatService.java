package com.eek.kimpli.chat.service;

import com.eek.kimpli.chat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    // ChatRepository 또는 필요한 다른 빈 주입
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

   public int getUnreadMessageCount(String roomNum) {
        return chatRepository.countByRoomNumAndRead(roomNum, false);
    }


}
