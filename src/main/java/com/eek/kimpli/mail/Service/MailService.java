package com.eek.kimpli.mail.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {

	//JavaMailSender 인터페이스의 객체를 선언

    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "leekleek9494@gmail.com";
    private static int number;

    public static void createNumber(){
       number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public MimeMessage CreateMail(String mail){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("[plikim]사이트에서 회원정보 비밀번호찾기 인증번호를 보내드립니다.");
            String body = "";
            body += "<h1>" + "[plikim] " + "</h1>";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            //body =내용
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String mail){

        MimeMessage message = CreateMail(mail);
        //실제 이메일 전송
        javaMailSender.send(message);

        return number;
    }
}