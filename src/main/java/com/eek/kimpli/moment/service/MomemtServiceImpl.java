package com.eek.kimpli.moment.service;

import com.eek.kimpli.moment.model.Moment;
import com.eek.kimpli.moment.model.MomentImg;
import com.eek.kimpli.moment.repository.MomentImgRepository;
import com.eek.kimpli.moment.repository.MomentRepository;
import com.eek.kimpli.moment.validator.MomentValidator;
import com.eek.kimpli.user.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor //생성자 만들어줌
public class MomemtServiceImpl implements MomentService {

    //    private final MomentValidator momentValidator;
    @Value("${external.upload.momentPath}")
    private String momentPath;
    private final MomentRepository momentRepository;
    private final MomentImgRepository momentImgRepository;

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

   @Override
    public String saveMoment(Moment moment, List<MultipartFile> momentImgs) {
     if (momentImgs != null && !momentImgs.isEmpty()) {
        int displayOrder = 1;  // 초기값 설정

        for (MultipartFile profileFile : momentImgs) {
            System.out.println("이미지리스트2" + momentImgs);

            // 업로드된 파일의 원본 파일 이름을 OriginalFilename로 설정
            String originalFilename = profileFile.getOriginalFilename();

            // MomentImg 엔티티 생성
            MomentImg momentImg = new MomentImg();
            momentImg.setMoment(moment);
            momentImg.setStoredFileName(System.currentTimeMillis() + "_" + originalFilename);
            momentImg.setOriginProfileImg(originalFilename);
            momentImg.setImgCreatedDate(LocalDateTime.now());
            momentImg.setDisplayOrder(displayOrder);  // display_order 설정

            // 스프링 프로퍼티 참조해서 저장 경로 설정
            String savePath = momentPath + momentImg.getStoredFileName();
            System.out.println("저장 경로 + 이름: " + savePath);

            // display_order 증가
            displayOrder++;

                // 서버에 파일 저장
                try {
                    FileService.saveFile(profileFile.getBytes(), savePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    // 예외 처리: 파일 저장 중에 문제가 발생한 경우
                    return "Error saving files";
                }

                // MomentImg 저장
                momentImgRepository.save(momentImg);


            }

        }

        // Moment 저장
        momentRepository.save(moment);

        return "Success";
    }


@Override
@Transactional
public List<Moment> findAll() {
    return momentRepository.findAll();
}

//@Override
//    public List<MomentImg> getImagesByMomentId(Long momentId) {
//        Moment moment = momentRepository.findById(momentId).orElse(null);
//        return moment != null ? moment.getMomentImg() : Collections.emptyList();
//    }

}




