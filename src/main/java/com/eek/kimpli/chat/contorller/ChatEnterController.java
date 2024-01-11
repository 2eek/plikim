package com.eek.kimpli.chat.contorller;

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

    @PostMapping("/chat/userLeave")
    public ResponseEntity<ChatEnterRecord> userLeave(@RequestBody ChatEnterRecord chatLeaveRecord) {
        chatLeaveRecord.setLastTime(LocalDateTime.now());
        chatLeaveRecord.setState(false);

        Mono<ChatEnterRecord> result = chatEnterRecordRepository.save(chatLeaveRecord);

        // Mono에 구독하여 저장 작업을 시작
        return ResponseEntity.ok().body(result.block()); // block()을 사용하여 Mono를 블록킹하고 결과를 반환
    }


    @MessageMapping("/userEnterCheck") // 클라이언트에서 메시지를 보낼 때 사용하는 주소
    @SendTo("/topic/userEnterCheck") // 클라이언트로 메시지를 보낼 때 사용하는 주소
    public Mono<ChatEnterRecord> chatEnter(@Payload ChatEnterRecord message) {
//        System.out.println("Received message: " + message);

        // 채팅 입장 기록 생성 또는 기존 기록 조회
        return chatEnterRecordRepository
            .findFirstBySenderAndReceiverAndRoomNumOrderByLastTimeDesc(
                message.getReceiver(),
                message.getSender(),
                message.getRoomNum()
            )
            .flatMap(existingRecord -> {
                // 기존 기록이 있는 경우
//                System.out.println("Existing record: " + existingRecord);
                return Mono.just(existingRecord);
            })
            .switchIfEmpty(Mono.defer(() -> {
                // 기존 기록이 없는 경우 새로운 기록 생성
                ChatEnterRecord newRecord = new ChatEnterRecord();
                newRecord.setSender(message.getReceiver());
                newRecord.setReceiver(message.getSender());
                newRecord.setRoomNum(message.getRoomNum());

//                System.out.println("Created new record: " + newRecord);

                return chatEnterRecordRepository.save(newRecord);
            }));
    }


    @PostMapping("/chat/userEnter")
    public ResponseEntity<ChatEnterRecord> userEnter(@RequestBody ChatEnterRecord chatEnterRecord) {
        chatEnterRecord.setLastTime(LocalDateTime.now());
        chatEnterRecord.setState(true);

        Mono<ChatEnterRecord> result = chatEnterRecordRepository.save(chatEnterRecord);

        // Mono에 구독하여 저장 작업을 시작
        return ResponseEntity.ok().body(result.block()); // block()을 사용하여 Mono를 블록킹하고 결과를 반환
    }

}
