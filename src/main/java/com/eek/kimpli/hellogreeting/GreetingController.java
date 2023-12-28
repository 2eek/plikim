package com.eek.kimpli.hellogreeting;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {


@MessageMapping("/hello")
@SendTo("/topic/greetings")
public Greeting greeting(HelloMessage message) throws Exception {
    // 메시지가 null 또는 undefined인 경우 처리
    if (message == null || message.getName() == null || message.getName().trim().isEmpty()) {
        // 혹은 다른 처리를 수행하거나 무시할 수 있습니다.
        return null; // 예시로 null을 반환하여 아무 응답도 보내지 않음
    }

    Thread.sleep(1000); // simulated delay
    return new Greeting(HtmlUtils.htmlEscape(message.getName()));
}

}