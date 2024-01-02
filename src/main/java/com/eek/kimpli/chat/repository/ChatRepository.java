//package com.eek.kimpli.chat.repository;
//
//import com.eek.kimpli.chat.model.Chat;
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
//import org.springframework.data.mongodb.repository.Tailable;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Repository
//public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
//
//    @Tailable
//    @Query("{ 'sender' : ?0, 'receiver' : ?1 }")
//    Flux<Chat> mFindBySender(String sender, String receiver);
//
//    @Tailable
//    @Query("{ 'roomNum': ?0 }")
//    Flux<Chat> mFindByRoomNum(String roomNum);
//
//    Mono<Void> findByRoomNumAndReadFalse(String roomNum);
//}
package com.eek.kimpli.chat.repository;

import com.eek.kimpli.chat.model.Chat;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface ChatRepository extends ReactiveMongoRepository <Chat, String> {

        //귓속말 할 때 사용한다.
    @Tailable// 커서를 안닫고 계속 유지한다. 그래서 데이터가 계속흘러들어옴 컨트롤러는 Flux로 응답한다.-> SSE라는 프로토콜을 쓴다
    @Query("{ 'sender' : ?0, 'receiver' : ?1 }")
    Flux<Chat> mFindBySender(String sender, String receiver);


	@Tailable
	@Query("{ 'roomNum': ?0 }")
	Flux<Chat> mFindByRoomNum(String roomNum);

    //채팅방 안 읽은 채팅 갯수
    int countByRoomNumAndRead(String roomNum, boolean read);

}

