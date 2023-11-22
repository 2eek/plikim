package com.eek.kimpli.board.service;

import com.eek.kimpli.board.model.Board;
import org.springframework.validation.BindingResult;

public interface BoardService {
    String saveOrUpdateBoard(Board board, BindingResult bindingResult);
    // 다른 필요한 서비스 메서드 추가 가능
}
