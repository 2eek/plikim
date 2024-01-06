//package com.eek.kimpli.chat.model;
//
//import com.eek.kimpli.user.model.User;
//
//import javax.persistence.*;
//
//@Entity
//public class ChatRoomUserMapping {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "chat_room_id")  // 이 부분이 수정되어야 함
//    private ChatRoomUserMapping chatRoom;  // 수정 전: private Chat chatRoom;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    // 생성자, 게터, 세터 등 생략
//}