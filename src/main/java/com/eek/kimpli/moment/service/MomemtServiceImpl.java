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
public class MomemtServiceImpl implements MomentService{

    private final MomentValidator momentValidator;
    private final MomentRepository momentRepository;


    @Override// 하나의 메서드로 게시글 작성, 업데이트 처리
    public String saveOrUpdateMoment(Moment moment, BindingResult bindingResult) {
        try {
            // 현재 사용자의 세션 정보를 가져와서 작성자로 설정
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // 작성자 이름으로 설정된 새로운 moment 객체 생성
            moment.setAuthor(username);

            // 유효성 검사
            momentValidator.validate(moment, bindingResult);

            if (bindingResult.hasErrors()) {
                System.out.println("Validation errors:");
                List<ObjectError> errors = bindingResult.getAllErrors();
                for (ObjectError error : errors) {
                    System.out.println(error.getDefaultMessage());
                }
                     System.out.println("??????????");
                return "moment/list"; // 오류가 있으면 폼으로 다시 이동
            }


            // 게시글 조회
            if (moment.getId() != null) {
                // 키 값인 id가 있으면 update
//               Moment existingBoard = momentRepository.findById(moment.getId()).orElse(null);
               moment.setUpdatedDate(LocalDateTime.now());
               momentRepository.save(moment);
            } else {
                // 키 값인 id가 없으면 그냥 저장
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
