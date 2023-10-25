package com.eek.kimpli.member.repository;


import com.eek.kimpli.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository //mybatis 기능별로 쿼리문 정의한다. 쿼리를 호출, 매개변수 넘겨주고  리턴값 리턴해줌
@RequiredArgsConstructor// 의존성 주입받기
public class MemberRepository {
    private final SqlSessionTemplate sql;//마이바티스에서 제공하는 클래스

    //회원가입
    public int save(MemberDTO memberDTO) {
        System.out.println("memberDTO = "+ memberDTO);
        return sql.insert("Member.save",memberDTO);  //MemberMapper.xml과 연결됨
    }

    //일반로그인
    public MemberDTO login(MemberDTO memberDTO) {

        return sql.selectOne("Member.login",memberDTO);
    }




}
