
package com.eek.kimpli.chat.contorller;

import com.eek.kimpli.EncryptionUtils.EncryptionUtils;
import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.LocalDateTime;

@CrossOrigin
@RequiredArgsConstructor
@RestController//데이터 리턴 서버
public class ChatController {


	//DI일어남. 여기에 인스턴스가 들어옴
    final ChatRepository chatRepository;

	// 귓속말 할때

	@GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
		return chatRepository.mFindBySender(sender, receiver)
				.subscribeOn(Schedulers.boundedElastic());
	}

//sender의 채팅 내용이 화면에 출력됨
@GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<Chat> findByRoomNum(@PathVariable String roomNum) {
    return chatRepository.mFindByRoomNum(roomNum)
            .map(chat -> {
                // 메시지 복호화
                String decryptedMessage = EncryptionUtils.decrypt(chat.getMsg());
                chat.setMsg(decryptedMessage);
                return chat;
            })
            .subscribeOn(Schedulers.boundedElastic());
}

//sender의 채팅 내용이 db에 저장됨
@PostMapping("/chat")
public Mono<Chat> setMsg(@RequestBody Chat chat) {
    chat.setCreatedAt(LocalDateTime.now());
    System.out.println("암호화테스트"+chat);

    // 메시지 암호화
    String encryptedMessage = EncryptionUtils.encrypt(chat.getMsg());
    chat.setMsg(encryptedMessage);

    return chatRepository.save(chat);
}



}





