package com.eek.kimpli.chat.contorller;

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


	@GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> findByRoomNum(@PathVariable Integer roomNum) {
		return chatRepository.mFindByRoomNum(roomNum)
				.subscribeOn(Schedulers.boundedElastic());
	}


	@PostMapping("/chat")
	public Mono<Chat> setMsg(@RequestBody Chat chat){
		chat.setCreatedAt(LocalDateTime.now());
		return chatRepository.save(chat); // Object를 리턴하면 자동으로 JSON 변환 (MessageConverter)
	}

//	    	@GetMapping("/chat")
//			public String chat(){
//				return "/chat/chat";
//			}

}




