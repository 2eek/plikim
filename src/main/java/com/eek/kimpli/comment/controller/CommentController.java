package com.eek.kimpli.comment.controller;

import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@RestController
@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }




    //댓글 ajax 저장
    @PostMapping("/save")
    @ResponseBody
    public String save(@Valid Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userId = userDetails.getUsername(); // 또는 다른 방식으로 userId를 가져와야 할 수 있습니다.
            System.out.println(userId);
            // 이제 userId를 이용하여 저장 로직을 수행합니다.
            comment.setCommentWriter(userId);

            String result = commentService.saveOrUpdateComment(comment);
            System.out.println("댓글 저장 결과: " + result);
            return result;
        } else {
            // 로그인되지 않은 경우에 대한 처리
            return "로그인이 필요합니다.";
        }
    }

       //댓글 화면에 뿌리기
    @GetMapping("/list/{boardId}")
    @ResponseBody
    public List<Comment> getCommentsByBoardId(@PathVariable Long boardId) {
        // 게시글 ID에 해당하는 댓글 목록을 가져오는 서비스 메서드 호출
        return commentService.getCommentsByBoardId(boardId);
    }



}
