//package com.eek.kimpli.comment.model;
//
//import com.eek.kimpli.comment.model.Comment;
//import com.eek.kimpli.replycomment.model.ReplyComment;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@Component
//public class CommentToReplyCommentConverter implements Converter<Comment, ReplyComment> {
//
//    @Override
//    public ReplyComment convert(Comment source) {
//        ReplyComment replyComment = new ReplyComment();
//        // 필요한 필드 복사
//        replyComment.setReplyCommentWriter(source.getCommentWriter());
//        replyComment.setReplyCommentContents(source.getCommentContents());
//
//        // Comment 엔터티의 id를 ReplyComment의 commentId로 설정
//        replyComment.setCommentId(source.getId());
//
//        // 다른 필드 복사...
//
//        return replyComment;
//    }
//}
