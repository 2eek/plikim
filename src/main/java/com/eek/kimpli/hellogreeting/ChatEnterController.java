package com.eek.kimpli.hellogreeting;

import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.model.ChatEnterRecord;
import com.eek.kimpli.chat.repository.ChatEnterRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class ChatEnterController {


    private final ChatEnterRecordRepository chatEnterRecordRepository;
////채팅방 퇴장시
//    //websocket연결 구독
//    @MessageMapping("/leaveChatRoom")
//    @SendTo("/topic/leaveChatRoom")
//    public Chat leaveChatRoom(Chat message) {
//        System.out.println("message = " + message);
//        System.out.println("방 나감? ");
//        Chat escapedMessage = new Chat();
//        escapedMessage.setSender(HtmlUtils.htmlEscape(message.getSender()));
//        escapedMessage.setReceiver(HtmlUtils.htmlEscape(message.getReceiver()));
//        escapedMessage.setRoomNum(HtmlUtils.htmlEscape(message.getRoomNum()));
//        // 채팅방에서 사용자 제거 또는 처리 로직 추가
//        return escapedMessage;
//    }

    @PostMapping("/chat/userLeave")
    public ResponseEntity<ChatEnterRecord> userLeave(@RequestBody ChatEnterRecord chatLeaveRecord) {
        System.out.println("chatLeaveRecord1 = " + chatLeaveRecord);
        chatLeaveRecord.setLastTime(LocalDateTime.now());
        chatLeaveRecord.setState(false);
        System.out.println("chatEnterRecord2 = " + chatLeaveRecord);

        Mono<ChatEnterRecord> result = chatEnterRecordRepository.save(chatLeaveRecord);

        // Mono에 구독하여 저장 작업을 시작
        return ResponseEntity.ok().body(result.block()); // block()을 사용하여 Mono를 블록킹하고 결과를 반환
    }


    @MessageMapping("/userEnterCheck") // 클라이언트에서 메시지를 보낼 때 사용하는 주소
    @SendTo("/topic/userEnterCheck") // 클라이언트로 메시지를 보낼 때 사용하는 주소
    public Mono<ChatEnterRecord> chatEnter(@Payload ChatEnterRecord message) {
        System.out.println("Received message: " + message);

        // 채팅 입장 기록 생성 또는 기존 기록 조회
        return chatEnterRecordRepository
            .findFirstBySenderAndReceiverAndRoomNumOrderByLastTimeDesc(
                message.getReceiver(),
                message.getSender(),
                message.getRoomNum()
            )
            .flatMap(existingRecord -> {
                // 기존 기록이 있는 경우
                System.out.println("Existing record: " + existingRecord);
                return Mono.just(existingRecord);
            })
            .switchIfEmpty(Mono.defer(() -> {
                // 기존 기록이 없는 경우 새로운 기록 생성
                ChatEnterRecord newRecord = new ChatEnterRecord();
                newRecord.setSender(message.getReceiver());
                newRecord.setReceiver(message.getSender());
                newRecord.setRoomNum(message.getRoomNum());

                System.out.println("Created new record: " + newRecord);

                return chatEnterRecordRepository.save(newRecord);
            }));
    }


    @PostMapping("/chat/userEnter")
    public ResponseEntity<ChatEnterRecord> userEnter(@RequestBody ChatEnterRecord chatEnterRecord) {
        System.out.println("chatEnterRecord1 = " + chatEnterRecord);
        chatEnterRecord.setLastTime(LocalDateTime.now());
        chatEnterRecord.setState(true);
        System.out.println("chatEnterRecord2 = " + chatEnterRecord);

        Mono<ChatEnterRecord> result = chatEnterRecordRepository.save(chatEnterRecord);

        // Mono에 구독하여 저장 작업을 시작
        return ResponseEntity.ok().body(result.block()); // block()을 사용하여 Mono를 블록킹하고 결과를 반환
    }

}
