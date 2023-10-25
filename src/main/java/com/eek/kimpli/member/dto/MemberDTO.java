package com.eek.kimpli.member.dto;

import lombok.Data;

@Data
public class MemberDTO {
    private String id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private String birthdate;
    private String memberMobile;
    private String gender;
    private String authority;
    private String access_token;
    private String refresh_token;
    private String sign_up_date;
    private String withdrawal_date;

}
