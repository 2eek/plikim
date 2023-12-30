package com.eek.kimpli.comment.controller;

import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.board.repository.BoardRepository;
import com.eek.kimpli.board.service.BoardService;
import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentHttpController {
        final BoardService boardService;
        final CommentService commentService;

                @GetMapping("/myCommentList")
        public String myCommentList(Model model, @PageableDefault(size = 5) Pageable pageable,
                                    @RequestParam(required = false, defaultValue = "") String searchText) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userDetails.getUsername();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("commentCreatedTime").descending());
        Page<Comment> comments = commentService.getCommentsByCommentWriter(userDetails.getUsername(), pageable);
        int currentPage = comments.getPageable().getPageNumber() + 1; // 현재 페이지 번호 (0부터 시작)
        int startPage = Math.max(1, currentPage - 2); // 현재 페이지 주변에 2 페이지씩 보여주기
        int endPage = Math.min(comments.getTotalPages(), startPage + 4);

          List<Comment> commentList = comments.getContent();
    List<String> boardTitles = new ArrayList<>();

    for (Comment comment : commentList) {
        Long boardId = comment.getBoardId();
        Board board = boardService.getBoardByBoardId(boardId);
        if (board != null) {
            boardTitles.add(board.getTitle());
        } else {
            // 해당 boardId에 해당하는 게시글이 없는 경우 처리
            boardTitles.add("No Title Found");
        }
    }


        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("comments", comments);
        model.addAttribute("boardTitles", boardTitles);
                    System.out.println("게시글제목?"+boardTitles);
        // 현재 사용자의 세션 정보
//        model.addAttribute("userSession", authentication.getPrincipal());
        return "board/myCommentList";
    }
}
