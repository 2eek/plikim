//package com.eek.kimpli.comment.converter;
//
//import com.eek.kimpli.comment.model.Comment;
//import com.eek.kimpli.replycomment.model.ReplyComment;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ReplyCommentToCommentConverter implements Converter<ReplyComment, Comment> {
//
//    @Override
//    public Comment convert(ReplyComment replyComment) {
//        Comment comment = new Comment();
//        // 여기서 ReplyComment에서 Comment로의 필드 복사 논리를 작성하세요.
//        comment.setCommentContents(replyComment.getReplyCommentContents());
//        // 필요한 다른 필드도 복사해야 합니다.
//
//        return comment;
//    }
//}
//
