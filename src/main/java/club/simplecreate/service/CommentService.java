package club.simplecreate.service;

import club.simplecreate.pojo.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentList(String articleId);

    boolean insertComment(Comment comment);

    List<Object> getReplyList(String commentId);
}
