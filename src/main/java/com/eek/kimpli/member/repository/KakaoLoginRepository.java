package com.eek.kimpli.member.repository;


import com.eek.kimpli.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
@RequiredArgsConstructor
public class KakaoLoginRepository {
    private final SqlSessionTemplate sql;


//    public int save(MemberDTO memberDTO){
//        System.out.println("memberDTO = " + memberDTO);
//        return sql.insert("Member.save",memberDTO);
//    }
      //카카오 로그인 시 정보 저장
    public int insertKakaoLogin(HashMap<String, Object> userInfo){
        //파인드 하고 인서트 한다.
        return sql.insert("Member.insertKakaoLogin",userInfo);
    }

	 public int updateKakaoLogin(HashMap<String, Object> userInfo){
        return sql.insert("Member.updateKakoLogin",userInfo);
     }

	 public MemberDTO findKakao(HashMap<String, Object> userInfo){
         return sql.selectOne("Member.findKakao",userInfo);
     }

//    public MemberDTO  find(HashMap<String, Object> userInfo) {
//        System.out.println("memberDTO = "+ userInfo);
//        return sql.selectOne("Member.login",userInfo);  //MemberMapper.xml과 연결됨
//    }
}
