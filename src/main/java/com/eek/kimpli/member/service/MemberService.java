package com.eek.kimpli.member.service;


import com.eek.kimpli.member.dto.MemberDTO;
import com.eek.kimpli.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //-> final이 붙은 필드만 가지고 생성자를 만듦. 생성자 주입
public class MemberService {
    private final MemberRepository memberRepository;

    public int save(MemberDTO memberDTO) {
        return memberRepository.save(memberDTO);
    }

    public int KakaoSave(MemberDTO memberDTO) {
        return memberRepository.save(memberDTO);
    }

    public boolean login(MemberDTO memberDTO) {
        MemberDTO loginMember = memberRepository.login(memberDTO);
        if (loginMember != null){
            return true;
        }else {
            return false;
        }
    }
}
