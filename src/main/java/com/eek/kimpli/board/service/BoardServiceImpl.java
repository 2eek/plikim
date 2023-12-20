package com.eek.kimpli.board.service;

import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.board.repository.BoardRepository;
import com.eek.kimpli.board.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardValidator boardValidator;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, BoardValidator boardValidator) {
        this.boardRepository = boardRepository;
        this.boardValidator = boardValidator;
    }

    @Override// 하나의 메서드로 게시글 작성, 업데이트 처리
    public String saveOrUpdateBoard(Board board, BindingResult bindingResult) {
        try {
            // 현재 사용자의 세션 정보를 가져와서 작성자로 설정
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // 작성자 이름으로 설정된 새로운 Board 객체 생성
            board.setAuthor(username);

            // 유효성 검사
            boardValidator.validate(board, bindingResult);

            if (bindingResult.hasErrors()) {
                System.out.println("Validation errors:");
                List<ObjectError> errors = bindingResult.getAllErrors();
                for (ObjectError error : errors) {
                    System.out.println(error.getDefaultMessage());
                }
                     System.out.println("??????????");
                return "board/writeForm"; // 오류가 있으면 폼으로 다시 이동
            }


            // 게시글 조회
            if (board.getId() != null) {
                // 키 값인 id가 있으면 update
                Board existingBoard = boardRepository.findById(board.getId()).orElse(null);

                if (existingBoard != null) {
                    // 조회수 유지
                    board.setViews(existingBoard.getViews());

                    // 현재 시간으로 갱신
                    board.setUpdatedDate(LocalDateTime.now());

                    // 이미 저장된 데이터를 업데이트
                    boardRepository.save(board);
                    System.out.println('B' );
                }
            } else {
                // 키 값인 id가 없으면 그냥 저장
                boardRepository.save(board);
            }
                 System.out.println("이쪽도 오나");
            return "redirect:list";

        } catch (Exception e) {
            System.out.println("설마");
            e.printStackTrace();
            throw e;
        }
    }
}
    // 다른 서비스 메서드를 필요에 따라 구현 가능

