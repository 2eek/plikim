package com.eek.kimpli.member.dto;

import lombok.Data;

@Data
public class MemberDTO {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private int memberAge; //회원가입 할 때 문자를 입력 받으면 400번 에러 뜬다! 자동으로 형변환 안해줌
    private String memberMobile;

}
