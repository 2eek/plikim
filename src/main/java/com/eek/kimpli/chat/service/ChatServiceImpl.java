package com.eek.kimpli.chat.service;

import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.model.ChatEnterRecord;
import com.eek.kimpli.chat.repository.ChatEnterRecordRepository;
import com.eek.kimpli.chat.repository.ChatRepository;
import com.eek.kimpli.encryptionUtils.AesUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
   private final ChatRepository chatRepository;
    private final ChatEnterRecordRepository chatEnterRecordRepository;

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    public ChatServiceImpl(ChatRepository chatRepository, ChatEnterRecordRepository chatEnterRecordRepository) {
        this.chatRepository = chatRepository;
        this.chatEnterRecordRepository = chatEnterRecordRepository;
    }

    @Override
    public Flux<Chat> getMessages(String sender, String receiver) {
               return chatRepository.mFindBySender(sender, receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Chat> sendMessage(Chat chat) {
         try {
            // 메세지 암호화해서 저장
            String encryptedMessage = AesUtil.aesCBCEncode(chat.getMsg());
            chat.setMsg(encryptedMessage);
            // 현재 시간 지정
            chat.setCreatedAt(LocalDateTime.now());
            ChatEnterRecord chatEnterRecord = new ChatEnterRecord();
            // 조회 시 상대방이 sender, 내가 receiver, 방 이름
            chatEnterRecord.setSender(chat.getReceiver());
            chatEnterRecord.setReceiver(chat.getSender());
            chatEnterRecord.setRoomNum(chat.getRoomNum());

            return chatEnterRecordRepository
                    .findFirstBySenderAndReceiverAndRoomNumOrderByLastTimeDesc(
                            chatEnterRecord.getSender(),
                            chatEnterRecord.getReceiver(),
                            chatEnterRecord.getRoomNum()
                    )
                    .hasElement()
                    .flatMap(hasElement -> {
                        if (hasElement) {
                            return chatEnterRecordRepository
                                    .findFirstBySenderAndReceiverAndRoomNumOrderByLastTimeDesc(
                                            chatEnterRecord.getSender(),
                                            chatEnterRecord.getReceiver(),
                                            chatEnterRecord.getRoomNum()
                                    )
                                    .map(record -> {
                                        if (record.isState()) {
                                            System.out.println("getState() is true");
                                            chat.setRead(0);
                                        } else {
                                            System.out.println("getState() is false");
                                            // 상대방이 채팅방에 한 번도 안들어 온 경우
                                            chat.setRead(1);
                                        }
                                        return chat;
                                    })
                                    .flatMap(chatRepository::save);
                        } else {
                            System.out.println("hasElement is false");
                            // element가 없는 경우(채팅 기록이 없는 경우)
                            return chatRepository.save(chat);
                        }
                    })
                    .onErrorResume(EmptyResultDataAccessException.class, e -> {
                        // 결과가 없는 경우 처리
                        e.printStackTrace();
                        return Mono.empty(); // 또는 다르게 처리하십시오.
                    })
                    .onErrorResume(Exception.class, e -> {
                        // 다른 예외 처리
                        e.printStackTrace();
                        return Mono.error(e);
                    })
                    .doOnError(e -> {
                        // 처리되지 않은 예외에 대한 추가 로깅
                        e.printStackTrace();
                    });
        } catch (Exception e) {
            // 암호화 관련 예외 처리
            e.printStackTrace();
            return Mono.error(e);
        }
    }

    @Override
    public Flux<Chat> getMessagesByRoomNum(String roomNum) {
        return Mono.defer(() -> {

                    return Mono.just(0); // Mono.just(0)을 반환하여 값을 전달하지 않음
                }).thenMany(chatRepository.mFindByRoomNum(roomNum)
                        .flatMap(chat -> {
                            try {

                                String decryptedMessage = AesUtil.aesCBCDecode(chat.getMsg());
                                chat.setMsg(decryptedMessage);

                                return Mono.just(chat);
                            } catch (Exception e) {
                                return Mono.error(e);
                            }
                        }))
                .doOnNext(c -> {
                    // 채팅이 처리되면 출력
//                    System.out.println("Mono 값: " + c);
//                    System.out.println("==============================");
                });
    }

//ChatViewController




       @Override
    public List<Chat> getAllChats() {
        Flux<Chat> chatRoomFlux = chatRepository.findAll();
        List<Chat> chatRoomList = chatRoomFlux.collectList().block();

        if (chatRoomList != null) {
            Set<String> uniqueRoomNums = new HashSet<>();

            chatRoomList = chatRoomList.stream()
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

        return chatRoomList;
    }

    @Override
    public List<Chat> getChatRoomsByUserName(String userName) {
        return null;
    }

    @Override
    public void markChatAsRead(String username, String roomNum) {

    }
        @Override
    public void processChat(String userId, String roomNum) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        ChatUpdater chatUpdater = new ChatUpdater();
        chatUpdater.markRead(username, roomNum, uri);  // uri는 어디서 가져오는지에 따라 수정이 필요합니다.

    }
}
