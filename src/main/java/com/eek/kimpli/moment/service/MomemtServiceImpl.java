package com.eek.kimpli.moment.service;

import com.eek.kimpli.moment.model.Moment;
import com.eek.kimpli.moment.repository.MomentRepository;
import com.eek.kimpli.moment.validator.MomentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor //생성자 만들어줌
public class MomemtServiceImpl implements MomentService {

    //    private final MomentValidator momentValidator;
    private final MomentRepository momentRepository;


    @Override// 하나의 메서드로 게시글 작성, 업데이트 처리
    public String saveOrUpdateMoment(Moment moment) {
        try {
            // 현재 사용자의 세션 정보를 가져와서 작성자로 설정
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // 작성자 이름으로 설정
            moment.setAuthor(username);

            // 게시글 조회
            //기존 모멘트를 업데이트
            if (moment.getId() != null) {
                moment.setUpdatedDate(LocalDateTime.now());
                momentRepository.save(moment);
            } else {
            //신규 모멘트 저장
            // 키 값인 id가 없으면 그냥 저장



               moment.setCreatedDate(LocalDateTime.now());
               momentRepository.save(moment);
            }
            System.out.println("이쪽도 오나");
            return "redirect:list";
        } catch (Exception e) {
            System.out.println("설마");
            e.printStackTrace();
            throw e;
        }
    }
}

