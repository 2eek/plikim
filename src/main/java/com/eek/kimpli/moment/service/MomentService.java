package com.eek.kimpli.moment.service;

import com.eek.kimpli.moment.model.Moment;
import org.springframework.validation.BindingResult;

public interface MomentService {

    String saveOrUpdateMoment(Moment moment, BindingResult bindingResult);
}
