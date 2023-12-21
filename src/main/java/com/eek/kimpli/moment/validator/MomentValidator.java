package com.eek.kimpli.moment.validator;

import com.eek.kimpli.moment.model.Moment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component // 이거 있어야 DI 가능하다
public class MomentValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Moment.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Moment m = (Moment) obj;
        if (StringUtils.isEmpty(m.getContent())) {
            errors.rejectValue("content", "key", "내용을 입력하세요");
        }


    }
}


