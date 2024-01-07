//package com.eek.kimpli.chat.repository;
//
//import com.eek.kimpli.chat.model.Chat;
//import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.data.mongodb.repository.Tailable;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Repository
//public class ChatUpRepository {
//
//    private final ReactiveMongoTemplate reactiveMongoTemplate;
//
//    public ChatUpRepository(ReactiveMongoTemplate reactiveMongoTemplate) {
//        this.reactiveMongoTemplate = reactiveMongoTemplate;
//    }
//
//    // ... (기존 코드 생략)
//
//    @Tailable
//    @org.springframework.data.mongodb.repository.Query("{ 'receiver': ?0, 'roomNum': ?1 }")
//    public Flux<Chat> mFindByReceiverAndRoomNum(String receiver, String roomNum) {
//        Query query = new Query(Criteria.where("receiver").is(receiver).and("roomNum").is(roomNum));
//        return reactiveMongoTemplate.tail(query, Chat.class);
//    }
//
//    public Mono<Void> updateReadStatus(String receiver, String roomNum, int read) {
//        Query query = new Query(Criteria.where("receiver").is(receiver).and("roomNum").is(roomNum));
//        Update update = new Update().set("read", read);
//
//        return reactiveMongoTemplate.updateFirst(query, update, Chat.class)
//                .then(Mono.empty());
//    }
//}
