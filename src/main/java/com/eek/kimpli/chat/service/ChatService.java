//
//package com.eek.kimpli.chat.service;
//
//import com.eek.kimpli.chat.model.Chat;
//import com.eek.kimpli.chat.model.ChatRoom;
//import com.eek.kimpli.chat.repository.ChatRepository;
//import com.eek.kimpli.chat.repository.ChatRoomUserMappingRepository;
//import com.eek.kimpli.encryptionUtils.AesUtil;
//import com.eek.kimpli.user.model.User;
//import com.mongodb.client.result.UpdateResult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//import java.time.LocalDateTime;
//
//@Service
//public class ChatService {
//  @Autowired
//    private ChatRepository chatRepository;
//  private  Chat chat;
//
//    @Autowired
//    private ChatRoomUserMappingRepository chatRoomUserMappingRepository;
//
//    @Autowired
//    private ChatUpdater chatUpdater; // ChatUpdater를 주입받아 사용
//
//    public Mono<Chat> setMsg(Chat chat) {
//        // 나와의 대화에서는 read에 기본으로 0 대입. 보내는이와 받는이가 같다면.
//        // 내가 저장하는 상황에서 항상 보내는이는 내가 되고, 받는이는 상대방이 된다.
//        if (chat.getSender().equals(chat.getReceiver())) {
//            chat.setRead(0);
//        }
//        chat.setCreatedAt(LocalDateTime.now());
//
//        try {
//            // Message encryption
//            String encryptedMessage = AesUtil.aesCBCEncode(chat.getMsg());
//            chat.setMsg(encryptedMessage);
//
//            Chat savedChat = chatRepository.save(chat);
//
//            // 채팅 저장 후 채팅방에 참여한 사용자 중 상대방이 있으면 markRead 실행
//            checkAndUpdateReadStatus(chat);
//
//            return Mono.just(savedChat);
//        } catch (Exception e) {
//            return Mono.error(e); // 예외 발생 시 Mono.error로 감싸서 반환
//        }
//    }
//
//    private void checkAndUpdateReadStatus(Chat chat) {
//        // 채팅방에 참여한 사용자 중 상대방이 있는지 확인
//        boolean isReceiverInChatRoom = chatRoomUserMappingRepository.existsByUserAndChatRoom(
//                new User(chat.getReceiver()), // 이는 예시일 뿐, 실제로는 사용자를 찾는 방법에 따라 다름
//                new ChatRoom(chat.getRoomNum())
//        );
//
//        // 상대방이 채팅방에 있으면 markRead 실행
//        if (isReceiverInChatRoom) {
//            chatUpdater.markRead(chat.getReceiver(), chat.getRoomNum(), uri);
//        }
//    }