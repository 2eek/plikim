package com.eek.kimpli.chat.contorller;

import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@CrossOrigin
@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService; // ChatService 주입

    //DB에 채팅 저장
    @PostMapping("/chat")//Jason으로 받는다.
    public Mono<Chat> setMsg(@RequestBody Chat chat) {
        return chatService.sendMessage(chat);
    }

    //화면에 flux 사용해서 흘려보내줌
    //SSE프로토콜 데이터가 생길 때 마다 지속적으로 데이터를 넘길 수 있다. response선이 안 끊기고 계속 유지된다.
    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> findByRoomNum(@PathVariable String roomNum) {
        return chatService.getMessagesByRoomNum(roomNum);
    }


}

