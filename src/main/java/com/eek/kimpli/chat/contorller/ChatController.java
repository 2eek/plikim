package com.eek.kimpli.chat.contorller;

import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.model.ChatEnterRecord;
import com.eek.kimpli.chat.repository.ChatEnterRecordRepository;
import com.eek.kimpli.chat.repository.ChatRepository;
import com.eek.kimpli.chat.service.ChatUpdater;
import com.eek.kimpli.encryptionUtils.AesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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


@PostMapping("/chat")
public Mono<Chat> setMsg(@RequestBody Chat chat) {
    chat.setCreatedAt(LocalDateTime.now());
    ChatEnterRecord chatEnterRecord = new ChatEnterRecord();
    chatEnterRecord.setSender(chat.getReceiver());
    chatEnterRecord.setReceiver(chat.getSender());
    chatEnterRecord.setRoomNum(chat.getRoomNum());

    return chatEnterRecordRepository
            .findFirstBySenderAndReceiverAndRoomNumOrderByLastTimeDesc(
                    chatEnterRecord.getSender(),
                    chatEnterRecord.getReceiver(),
                    chatEnterRecord.getRoomNum()
            )
            .flatMap(result -> {
                // 비동기 결과 처리
                // chatEnterRecord에 대한 작업 수행
                System.out.println("Received ChatEnterRecord: " + result);

                try {
                    if (result.isState()) {
                        // ChatEnterRecord의 state가 true인 경우에만 복호화 및 저장
                        String encryptedMessage = AesUtil.aesCBCEncode(chat.getMsg());
                        chat.setMsg(encryptedMessage);
                        chat.setRead(0);
                        return chatRepository.save(chat);
                    } else {
                        // ChatEnterRecord의 state가 false인 경우에만 복호화 및 저장
                        String encryptedMessage = AesUtil.aesCBCEncode(chat.getMsg());
                        chat.setMsg(encryptedMessage);
                        chat.setRead(1);
                        return chatRepository.save(chat);
                    }
                } catch (Exception e) {
                    // 복호화 실패 시 에러 처리
                    return Mono.error(new RuntimeException("Error encrypting/decrypting message", e));
                }
            })
            .doOnError(throwable -> {
                // 에러 처리
                throwable.printStackTrace();
            });
}

        private void sendNotificationToClient (String userId, String message){
            // 여기에서 WebSocket을 이용하여 클라이언트에게 메시지를 전송하는 코드를 작성합니다.
            messagingTemplate.convertAndSendToUser(userId, "/topic/notification", message);
        }

        public static String getRemainingWord (String input, String wordToRemove){
            if (input.contains(wordToRemove)) {
                return input.replace(wordToRemove, "").trim();
            } else {
                // wordToRemove가 input에 없을 경우 예외 처리 또는 특별한 값을 반환할 수 있습니다.
                return input;
            }
        }

        @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public Flux<Chat> findByRoomNum (@PathVariable String roomNum){

            System.out.println("test");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = ((UserDetails) authentication.getPrincipal()).getUsername();

            // chatUpdater.markRead를 Mono.defer 내에 래핑하여 동기화
            return Mono.defer(() -> {

                        return Mono.just(0); // Mono.just(0)을 반환하여 값을 전달하지 않음
                    }).thenMany(chatRepository.mFindByRoomNum(roomNum)
                            .flatMap(chat -> {
                                try {
//                                      ChatUpdater chatUpdater = new ChatUpdater();
//                                      chatUpdater.markRead(userId, roomNum, uri);
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

