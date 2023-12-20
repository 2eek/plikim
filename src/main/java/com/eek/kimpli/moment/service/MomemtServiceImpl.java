package com.eek.kimpli.moment.service;

import com.eek.kimpli.moment.dto.Moment;
import com.eek.kimpli.moment.repository.MomentRespository;
import com.eek.kimpli.moment.validator.MomentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor //생성자 만들어줌
public class MomemtServiceImpl implements MomentService{

    private final MomentValidator momentValidator;
    private final MomentRespository momentRespository;

    @Override
      public String saveOrUpdateMoment(Moment moment, BindingResult bindingResult){
return "a";
      }
}
