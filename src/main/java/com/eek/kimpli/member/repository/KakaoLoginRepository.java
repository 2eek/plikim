package com.eek.kimpli.member.repository;


import com.eek.kimpli.member.dto.MemberDTO;
import com.eek.kimpli.user.model.User;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
@RequiredArgsConstructor
public class KakaoLoginRepository {
    private final SqlSessionTemplate sql;

    public int insertKakaoLogin(HashMap<String, Object> userInfo) {
        return sql.insert("User.insertKakaoLogin", userInfo);
    }

    public int updateKakaoLogin(HashMap<String, Object> userInfo) {
        return sql.update("User.updateKakoLogin", userInfo);
    }

    public User findKakao(HashMap<String, Object> userInfo) {
        return sql.selectOne("User.findKakao", userInfo);
    }
}