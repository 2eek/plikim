package com.eek.kimpli.chat.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Document(collection = "chat")
public class Chat {
    @Id
    private String id;
    private String msg;
    private String sender; // 보내는 사람
    private String receiver; // 받는 사람
    private String roomNum; // 방 번호
    private LocalDateTime createdAt;
    private int read; // 읽음 여부를 나타내는 int 안 읽었으면 1 읽었으면 0 -> 합산할 때 1의 갯수를 더하면 됨. 각 채팅방 또한 read가 1인 것들의 합산
    private String formattedCreatedAt; // 추가된 부분
    // 생성자에서 read 값을 기본으로 1로 설정
    public Chat() {
        this.read = 1;
    }

//    @ManyToOne
//    @JoinColumn(name = "roomNum", referencedColumnName = "roomNum")
//    private ChatRoom chatRoom;
}