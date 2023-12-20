package com.eek.kimpli.chat.contorller;

import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.repository.ChatRepository;
import com.eek.kimpli.encryptionUtils.AesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class ChatController {

    final ChatRepository chatRepository;

    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
        return chatRepository.mFindBySender(sender, receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat) {
        chat.setCreatedAt(LocalDateTime.now());

        try {
            // Message encryption
            String encryptedMessage = AesUtil.aesCBCEncode(chat.getMsg());
            chat.setMsg(encryptedMessage);

            return chatRepository.save(chat);
        } catch (Exception e) {
            return Mono.error(e); // 예외 발생 시 Mono.error로 감싸서 반환
        }
    }

    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> findByRoomNum(@PathVariable String roomNum) {
        return chatRepository.mFindByRoomNum(roomNum)
                .flatMap(chat -> {
                    try {
                        // Message decryption
                        String decryptedMessage = AesUtil.aesCBCDecode(chat.getMsg());
                        chat.setMsg(decryptedMessage);
                        return Mono.just(chat);
                    } catch (Exception e) {
                        return Mono.error(e); // 예외 발생 시 Mono.error로 감싸서 반환
                    }
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
