package com.eek.kimpli.chat.repository;

import com.eek.kimpli.chat.model.ChatEnterRecord;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface ChatEnterRecordRepository extends ReactiveMongoRepository <ChatEnterRecord, String> {

Mono<ChatEnterRecord> findFirstBySenderAndReceiverAndRoomNumOrderByLastTimeDesc(String sender, String receiver, String roomNum);


}
