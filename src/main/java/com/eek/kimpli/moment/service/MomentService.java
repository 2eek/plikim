package com.eek.kimpli.moment.service;

import com.eek.kimpli.moment.model.Moment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MomentService {

    Moment saveOrUpdateMoment(Moment moment);
   String saveMoment(Moment moment, List<MultipartFile> momentImgs);

   List<Moment> findAll();
//   List<MomentImg> getImagesByMomentId(Long momentId);


}
