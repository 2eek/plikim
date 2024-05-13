package com.eek.kimpli.chat.repository;

import com.eek.kimpli.chat.model.Chat;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Repository
public interface ChatRepository extends ReactiveMongoRepository <Chat, String> {

    //귓속말 할 때 사용한다.
    @Tailable// 커서를 안닫고 계속 유지한다. 그래서 데이터가 계속흘러들어옴 컨트롤러는 Flux로 응답한다.-> SSE라는 프로토콜을 쓴다
    @Query("{ 'sender' : ?0, 'receiver' : ?1 }")
    Flux<Chat> mFindBySender(String sender, String receiver); //flux 흐름 데이터를 계속 받겠다
    
//반복적으로 가져온다.
    @Tailable
    @Query("{ 'roomNum': ?0 }")
    Flux<Chat> mFindByRoomNum(String roomNum);

//상대방 채팅방에 존재하는지 유무

}