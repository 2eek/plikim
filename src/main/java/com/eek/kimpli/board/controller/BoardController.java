package com.eek.kimpli.board.controller;

import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.board.repository.BoardRepository;
import com.eek.kimpli.board.validator.BoardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    // DI일어남. 여기에 인스턴스가 들어옴
    final BoardRepository boardRepository;
    final BoardValidator boardValidator;

    // 페이징+검색
    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 5) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText) {
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
        int currentPage = boards.getPageable().getPageNumber() + 1; // 현재 페이지 번호 (0부터 시작)
        int startPage = Math.max(1, currentPage - 2); // 현재 페이지 주변에 2 페이지씩 보여주기
        int endPage = Math.min(boards.getTotalPages(), startPage + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        System.out.println("게시물 리스트?"+boards);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 사용자의 세션 정보
        model.addAttribute("userSession", authentication.getPrincipal());
        return "board/list";
    }

    // 글 작성폼
    @GetMapping("/writeForm")
    public String writeForm(Model model) {
        // 로그인한 회원 값 넘기기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // 작성자 이름으로 셋팅된 새로운 Board 객체 생성
        Board board = new Board();
        board.setAuthor(username);
        model.addAttribute("board", board);

        return "board/writeForm";
    }

    // 글 저장 메서드
    @PostMapping("/save")
    public String save(@Valid Board board, BindingResult bindingResult) {
        System.out.println("Received data: " + board.toString());
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
                    System.out.println('B');
                }
            } else {
                // 키 값인 id가 없으면 그냥 저장
                boardRepository.save(board);
            }

            return "redirect:list";

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // 게시글 상세보기 -> 수정 가능
    @GetMapping("/detail")
    public String detail(Model model, @RequestParam(required = false) Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Board board = boardRepository.findById(id).orElse(null);
        // 조회수 증가
        board.setViews(board.getViews() + 1);

        // DB에 업데이트
        boardRepository.save(board);
        // 게시물 상세보기
        model.addAttribute("board", board);
        model.addAttribute("username", username);
        System.out.println("아이디 값 있을 때:" + board);

        return "board/detail";
    }
}
