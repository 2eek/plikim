//package com.eek.kimpli.chat.contorller;
//
//import com.eek.kimpli.chat.model.Chat;
//import com.eek.kimpli.chat.repository.ChatRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;
//
//import java.time.LocalDateTime;
//
//@CrossOrigin
//@RequiredArgsConstructor
//@RestController
//public class ChatController {
//
//    final ChatRepository chatRepository;
//    final SimpMessagingTemplate messagingTemplate; // WebSocket 알림을 위한 템플릿 추가
//
//    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
//        return chatRepository.mFindBySender(sender, receiver)
//                .subscribeOn(Schedulers.boundedElastic());
//    }
//
//    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<Chat> findByRoomNum(@PathVariable String roomNum) {
//        return chatRepository.mFindByRoomNum(roomNum)
//                .subscribeOn(Schedulers.boundedElastic());
//    }
//
//    @PostMapping("/chat")
//    public Mono<Chat> setMsg(@RequestBody Chat chat) {
//        chat.setCreatedAt(LocalDateTime.now());
//        chat.setRead(false);
//        chatRepository.save(chat);
//        sendNotificationToUser(chat.getReceiver());
//        return Mono.just(chat);
//    }
//
////@GetMapping(value = "/chat/roomNum/{roomNum}/notification-check", produces = MediaType.APPLICATION_JSON_VALUE)
////public Mono<Chat> markNotificationsAsRead(@PathVariable String roomNum) {
////    // 여기에 알림 확인 로직 추가
////    // 클라이언트에서 알림 확인 요청이 오면 읽음 플래그를 true로 설정
////    return chatRepository.findByRoomNumAndReadFalse(roomNum)
////            .flatMap(chat -> {
////                chat.setRead(true); // 여기에 setRead를 사용
////                return chatRepository.save(chat).then(Mono.just(chat));
////            });
////}
//
//
//    private void sendNotificationToUser(String username) {
//        // WebSocket을 통해 해당 사용자에게 알림을 전송
//        messagingTemplate.convertAndSendToUser(username, "/queue/notification", "You have a new chat message!");
//    }
//}
package com.eek.kimpli.chat.contorller;

import com.eek.kimpli.chat.model.Chat;
import com.eek.kimpli.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
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

//sender의 채팅 내용이 db에 저장됨
	@GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> findByRoomNum(@PathVariable String roomNum) {
		return chatRepository.mFindByRoomNum(roomNum)
				.subscribeOn(Schedulers.boundedElastic());
	}


	@PostMapping("/chat")
	public Mono<Chat> setMsg(@RequestBody Chat chat) {
		chat.setCreatedAt(LocalDateTime.now());
		return chatRepository.save(chat); // Object를 리턴하면 자동으로 JSON 변환 (MessageConverter)
	}



}





