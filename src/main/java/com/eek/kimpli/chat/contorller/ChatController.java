package com.eek.kimpli.chat.contorller;

import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.model.RoomStatus;
import com.eek.kimpli.chat.repository.ChatRepository;
import com.eek.kimpli.chat.service.ChatUpdater;
import com.eek.kimpli.encryptionUtils.AesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class ChatController {
 private final SimpMessagingTemplate messagingTemplate;


    final ChatRepository chatRepository;
    private final ChatUpdater chatUpdater;

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

    // 상대방이 존재하는지 확인
    return chatRepository.existsByReceiver(chat.getReceiver())
            .flatMap(hasReceiver -> {
                // 나와의 대화에서는 read에 기본으로 0 대입
                if (chat.getSender().equals(chat.getReceiver()) || !hasReceiver) {
                    chat.setRead(0);
                }

                try {
                    // Message encryption
                    String encryptedMessage = AesUtil.aesCBCEncode(chat.getMsg());
                    chat.setMsg(encryptedMessage);

                    return chatRepository.save(chat);
                } catch (Exception e) {
                    return Mono.error(new RuntimeException("Error encrypting message", e));
                }
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Failed to check if receiver exists")));
}
@PostMapping("/chat/roomEntered")
public ResponseEntity<RoomStatus> notifyRoomEntered(@RequestBody RoomStatus roomStatus,@RequestBody String receiver) {
    // 여기서 roomStatus에는 클라이언트에서 전송한 채팅방 상태 데이터가 들어옵니다.
    // 예를 들어, userId와 roomNum이 들어올 것입니다.
   if (roomStatus.getUserId().equals(receiver)) {
        sendNotificationToClient(roomStatus.getUserId(), "상대방이 채팅방에 들어왔습니다.");
    }
     System.out.println("상태"+roomStatus);
     System.out.println("방이름"+roomStatus.getRoomNum());
     System.out.println("유저아이디?"+roomStatus.getUserId());
    // 여기에 채팅방 상태를 업데이트하거나 다른 작업을 수행할 수 있습니다.
    // 예를 들어, 상태를 DB에 저장하는 등의 작업
    System.out.println("여기???? 들어옴?");

///topic/roomStatus 토픽으로 roomStatus 객체를 전송
    // 업데이트된 채팅방 상태를 모든 클라이언트에게 브로드캐스트
    messagingTemplate.convertAndSend("/topic/roomStatus", roomStatus);
    messagingTemplate.convertAndSend("/topic/greetings", "{\"userId\": \"" + roomStatus.getUserId() + "\", \"message\": \"입장했습니다.\"}");


    // 응답으로 업데이트된 roomStatus를 JSON으로 반환합니다.
    return ResponseEntity.ok(roomStatus);
}
private void sendNotificationToClient(String userId, String message) {
    // 여기에서 WebSocket을 이용하여 클라이언트에게 메시지를 전송하는 코드를 작성합니다.
   messagingTemplate.convertAndSendToUser(userId, "/topic/notification", message);
}

    public static String getRemainingWord(String input, String wordToRemove) {
        if (input.contains(wordToRemove)) {
            return input.replace(wordToRemove, "").trim();
        } else {
            // wordToRemove가 input에 없을 경우 예외 처리 또는 특별한 값을 반환할 수 있습니다.
            return input;
        }
    }

//    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<Chat> findByRoomNum(@PathVariable String roomNum) {
//
//        System.out.println("hi");
//
//                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//   String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
//            chatUpdater.markRead(userId, roomNum, uri);
//

    //    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<Chat> findByRoomNum(@PathVariable String roomNum) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
//
//        return chatRepository.mFindByRoomNum(roomNum)
//                .flatMap(chat -> {
//                    try {
//                        //이쪽으로 오는중
//                        System.out.println("test2");
//                        String decryptedMessage = AesUtil.aesCBCDecode(chat.getMsg());
//                        chat.setMsg(decryptedMessage);
//
//                        if (chat.getReceiver().equals(userId)) {
//                            chatUpdater.markRead(userId, roomNum, uri);
//                            System.out.println("업데이트했음");
//                            chat.setRead(0);
//                            Mono<Chat> monoChat = Mono.just(chat);
//                            monoChat.subscribe(c -> System.out.println("Mono 값: " + c));
//
//                            return Mono.just(chat);
//                        } else {
//                            System.out.println("업데이트 한게 없다 = 상대방이 안읽었다");
//                            return Mono.just(chat);
//                        }
//
//                    } catch (Exception e) {
//                        return Mono.error(e);
//                    }
//                });
//    }
    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> findByRoomNum(@PathVariable String roomNum) {

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
//
//      @PostMapping("/chat/updateRead")
//    public ResponseEntity<String> updateReadStatus(@RequestBody UpdateReadRequest request) {
//
//        return ResponseEntity.ok("Read status updated successfully");
//    }

}

