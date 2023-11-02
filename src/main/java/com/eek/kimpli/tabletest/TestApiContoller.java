package com.eek.kimpli.tabletest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestApiContoller {

    @Autowired
    Memberrepository1 memberRepository1;

    @GetMapping
    String test(){
        Member1 member1 = Member1.builder()
                .id(1L)
                .name("스프링")
                .build();
        System.out.println(member1.toString());
        memberRepository1.save(member1);

        return "test";
    }
}
