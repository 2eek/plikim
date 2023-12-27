package com.eek.kimpli.comment.controller;

//import com.eek.kimpli.comment.converter.CommentConverter;
import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.comment.service.CommentService;
import com.eek.kimpli.moment.model.Moment;
import com.eek.kimpli.replycomment.dto.ReplyCommentDTO;
import com.eek.kimpli.replycomment.model.ReplyComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

//       @Autowired
//    private CommentConverter commentConverter;



    //댓글 ajax 저장
    //save 메서드는 HTML 폼을 통해 데이터를 받아옴
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

    // 댓글 화면에 뿌리기 (페이징 처리)
 @GetMapping("/list/{boardId}")
    public ResponseEntity<Page<Comment>> getCommentsByBoardId(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentService.getCommentsByBoardId(boardId, pageable);
        if (commentsPage == null || commentsPage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(commentsPage, HttpStatus.OK);
    }

//대댓글Ajax저장
//JSON 데이터를 처리합니다. @RequestBody 어노테이션을 통해 HTTP 요청 본문에 있는 JSON 데이터를 ReplyComment 객체로 변환합니다. 또한, URL 쿼리 매개변수인 commentId를 받아옴
@PostMapping("/replySave")
public ResponseEntity<String> saveReplyComment(
    @RequestBody ReplyComment replyComment,
    @RequestParam Long commentId
) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
        replyComment.setReplyCommentCreatedTime(LocalDateTime.now());
        replyComment.setDeleted((byte) 0);

        commentService.saveOrUpdateReplyComment(replyComment, commentId);

        return ResponseEntity.ok("댓글과 답글이 성공적으로 저장되었습니다.");
    } else {
        // 로그인되지 않은 경우에 대한 처리
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }
}

@GetMapping("/getReplyComments/{commentId}")

    public ResponseEntity<Comment> getCommentWithReplies(@PathVariable Long commentId) {
        Comment commentWithReplies = commentService.getCommentWithReplies(commentId);

        if (commentWithReplies != null) {
            return new ResponseEntity<>(commentWithReplies, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}