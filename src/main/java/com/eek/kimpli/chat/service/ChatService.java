
package com.eek.kimpli.chat.service;

import com.eek.kimpli.chat.model.Chat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ChatService {

    // ChatController
    Flux<Chat> getMessages(String sender, String receiver);

    Mono<Chat> sendMessage(Chat chat);

    Flux<Chat> getMessagesByRoomNum(String roomNum);


// ChatViewController


     List<Chat> getAllChats();

    List<Chat> getChatRoomsByUserName(String userName);
        void processChat(String userId, String roomNum);


    void markChatAsRead(String username, String roomNum);

    // 다른 필요한 메서드들을 추가할 수 있습니다.

}