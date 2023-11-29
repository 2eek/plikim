package com.eek.kimpli.comment.controller;

import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.comment.service.CommentService;
import com.eek.kimpli.replycoment.model.ReplyComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
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
//    @GetMapping("/list/{boardId}")
//    @ResponseBody
//    public List<Comment> getCommentsByBoardId(@PathVariable Long boardId) {
//        // 게시글 ID에 해당하는 댓글 목록을 가져오는 서비스 메서드 호출
//        return commentService.getCommentsByBoardId(boardId);
//    }


    // 댓글 화면에 뿌리기 (페이징 처리)
 @GetMapping("/list/{boardId}")
    public ResponseEntity<Page<Comment>> getCommentsByBoardId(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentService.getCommentsByBoardId(boardId, pageable);
System.out.println("Current Page: " + commentsPage.getNumber());
    System.out.println("Total Pages: " + commentsPage.getTotalPages());
    System.out.println("Total Elements: " + commentsPage.getTotalElements());
    System.out.println("Is First Page: " + commentsPage.isFirst());
    System.out.println("Is Last Page: " + commentsPage.isLast());
    System.out.println("Has Previous Page: " + commentsPage.hasPrevious());
    System.out.println("Has Next Page: " + commentsPage.hasNext());
        if (commentsPage == null || commentsPage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(commentsPage, HttpStatus.OK);
    }
//대댓글Ajax저장
@PostMapping("/replySave")
public ResponseEntity<String> saveCommentAndReply(@RequestParam Long parentCommentId) {
    ReplyComment replyComment = new ReplyComment();
    replyComment.setReplyCommentWriter("Alice");
    replyComment.setReplyCommentContents("Replying to the comment.");
    replyComment.setBoardId(1L);
    replyComment.setCommentCreatedTime(LocalDateTime.now());
    replyComment.setDeleted((byte) 0);
    commentService.saveOrUpdateReplyComment(replyComment, parentCommentId);

    return ResponseEntity.ok("Comment and reply saved successfully.");
}


}
