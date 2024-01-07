package com.eek.kimpli.hellogreeting;

import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.model.ChatEnterRecord;
import com.eek.kimpli.chat.repository.ChatEnterRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
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




    @MessageMapping("/leaveChatRoom")
    @SendTo("/topic/leaveChatRoom")
    public Chat leaveChatRoom(Chat message) {
        System.out.println("message = " + message);
        System.out.println("방 나감? ");
        Chat escapedMessage = new Chat();
        escapedMessage.setSender(HtmlUtils.htmlEscape(message.getSender()));
        escapedMessage.setReceiver(HtmlUtils.htmlEscape(message.getReceiver()));
        escapedMessage.setRoomNum(HtmlUtils.htmlEscape(message.getRoomNum()));
        // 채팅방에서 사용자 제거 또는 처리 로직 추가
        return escapedMessage;
    }

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



       @MessageMapping("/chatEnter")
    @SendTo("/topic/chatEnter")
    public Chat chatEnter(Chat message) throws Exception {
        // 메시지가 null 또는 undefined인 경우 처리
        if (message == null || message.getReceiver() == null || message.getRoomNum().trim().isEmpty()) {
            // 혹은 다른 처리를 수행하거나 무시할 수 있다.
            return null; // 예시로 null을 반환하여 아무 응답도 보내지 않음
        }

        // Chat 객체 생성 및 필드에 HTML 이스케이프 적용
        Chat escapedMessage = new Chat();
        escapedMessage.setSender(HtmlUtils.htmlEscape(message.getSender()));
        escapedMessage.setReceiver(HtmlUtils.htmlEscape(message.getReceiver()));
        escapedMessage.setRoomNum(HtmlUtils.htmlEscape(message.getRoomNum()));

        Thread.sleep(1000); // simulated delay
        return escapedMessage;
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

//
//   @PostMapping("api/leaveChat")
//    public ResponseEntity<String> leaveChat() {
//        // 여기에 사용자가 채팅방을 나갈 때 수행할 로직을 추가합니다.
//        // 예를 들어, 채팅방에서 사용자를 제거하거나 상태를 업데이트하는 등의 작업을 수행합니다.
//
//        // 성공적으로 처리되었다면 200 OK를 반환합니다.
//        return ResponseEntity.ok("Leave chat successful");
//    }
}
