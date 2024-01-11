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

    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
        return chatService.getMessages(sender, receiver);
    }

    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat) {
        return chatService.sendMessage(chat);
    }

    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> findByRoomNum(@PathVariable String roomNum) {
        return chatService.getMessagesByRoomNum(roomNum);
    }


}

