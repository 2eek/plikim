package com.eek.kimpli.moment.service;

import com.eek.kimpli.moment.model.Moment;
import com.eek.kimpli.moment.model.MomentImg;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MomentService {

    String saveOrUpdateMoment(Moment moment);
   String saveMoment(Moment moment, List<MultipartFile> momentImgs);

   List<Moment> findAll();
//   List<MomentImg> getImagesByMomentId(Long momentId);


}
