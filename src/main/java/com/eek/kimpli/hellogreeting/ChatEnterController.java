package com.eek.kimpli.hellogreeting;

import com.eek.kimpli.chat.model.Chat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
@Controller
public class ChatEnterController {

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
}