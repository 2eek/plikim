package com.eek.kimpli.moment.service;

import com.eek.kimpli.moment.model.Moment;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MomentService {

    String saveOrUpdateMoment(Moment moment);
   String saveMoment(Moment moment, List<MultipartFile> profileFiles);
}
