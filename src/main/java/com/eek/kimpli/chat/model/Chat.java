package com.eek.kimpli.chat.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Document(collection="chat")
public class Chat {
    @Id
    //@GeneratedValue
    private String id;
    private String msg;
    private String sender;//보내는 사람
    private String receiver;//받는 사람
    private String roomNum; //방 번호
    private LocalDateTime createdAt;
    private boolean read; // 읽음 여부 플래그

   public void setRead(boolean read) {
        this.read = read;
    }
}
