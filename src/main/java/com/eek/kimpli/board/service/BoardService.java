package com.eek.kimpli.board.service;

import com.eek.kimpli.board.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface BoardService {
    String saveOrUpdateBoard(Board board, BindingResult bindingResult);
 Page<Board> findMyList(String userId, Pageable pageable);

         Board getBoardByBoardId(Long id);
}
