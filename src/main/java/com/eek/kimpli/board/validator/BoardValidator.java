package com.eek.kimpli.board.validator;

import com.eek.kimpli.board.model.Board;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component // 이거 있어야 DI 가능하다
public class BoardValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Board.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Board b = (Board) obj;
        if (StringUtils.isEmpty(b.getContent())) {
            errors.rejectValue("content", "key", "내용을 입력하세요");
        }

//        // 작성자(author)에 대한 유효성 검사
//        if (StringUtils.isEmpty(b.getAuthor())) {
//            errors.rejectValue("author", "key", "작성자를 입력하세요");
//
//        }

    }
}
