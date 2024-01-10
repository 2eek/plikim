package com.eek.kimpli.chat.contorller;

import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.model.ChatEnterRecord;
import com.eek.kimpli.chat.repository.ChatEnterRecordRepository;
import com.eek.kimpli.chat.repository.ChatRepository;
import com.eek.kimpli.chat.service.ChatUpdater;
import com.eek.kimpli.encryptionUtils.AesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;


@CrossOrigin
@RequiredArgsConstructor
@RestController
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;


    private final ChatRepository chatRepository;
    private final ChatUpdater chatUpdater;
    private final ChatEnterRecordRepository chatEnterRecordRepository;

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
        return chatRepository.mFindBySender(sender, receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }

    //        @PostMapping("/chat")
//    public Mono<Chat> setMsg(@RequestBody Chat chat) {
//        chat.setCreatedAt(LocalDateTime.now());
//
//        try {
//            // Message encryption
//            String encryptedMessage = AesUtil.aesCBCEncode(chat.getMsg());
//            chat.setMsg(encryptedMessage);
//
//            return chatRepository.save(chat);
//        } catch (Exception e) {
//            return Mono.error(e); // 예외 발생 시 Mono.error로 감싸서 반환
//        }
//    }
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat) {
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


    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> findByRoomNum(@PathVariable String roomNum) {

        System.out.println("test");

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
                    System.out.println("Mono 값: " + c);
                    System.out.println("==============================");
                });
    }


}

