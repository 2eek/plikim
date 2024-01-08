//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@PostMapping("/chat")
//public Mono<Chat> setMsg(@RequestBody Chat chat) {
//    try {
//        // ... (기존 코드)
//
//        return chatEnterRecordRepository
//                .findFirstBySenderAndReceiverAndRoomNumOrderByLastTimeDesc(
//                        chatEnterRecord.getSender(),
//                        chatEnterRecord.getReceiver(),
//                        chatEnterRecord.getRoomNum()
//                )
//                .map(chatEnterRecord -> {
//                    if (chatEnterRecord.getState()) {
//                        System.out.println("getState() is true");
//                        chat.setRead(0);
//                    } else {
//                        System.out.println("getState() is false");
//                        chat.setRead(1);
//                    }
//                    return chat;
//                })
//                .flatMap(chatRepository::save)
//                .onErrorResume(EmptyResultDataAccessException.class, e -> {
//                    // 결과가 없는 경우 처리
//                    e.printStackTrace();
//                    return Mono.empty(); // 또는 다르게 처리하십시오.
//                })
//                .onErrorResume(Exception.class, e -> {
//                    // 다른 예외 처리
//                    e.printStackTrace();
//                    return Mono.error(e);
//                })
//                .doOnError(e -> {
//                    // 처리되지 않은 예외에 대한 추가 로깅
//                    e.printStackTrace();
//                });
//    } catch (Exception e) {
//        // 암호화 관련 예외 처리
//        e.printStackTrace();
//        return Mono.error(e);
//    }
//}
