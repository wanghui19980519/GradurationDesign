package club.simplecreate.service.impl;

import club.simplecreate.cache.CommentCache;
import club.simplecreate.dao.CommentMapper;
import club.simplecreate.pojo.Comment;
import club.simplecreate.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentCache commentCache;
    @Autowired
    CommentMapper commentMapper;
    @Override
    public List<Comment> getCommentList(String articleId) {
        return commentCache.getCommentList(articleId);
    }

    @Override
    public boolean insertComment(Comment comment) {
        //加入数据库
        if(commentMapper.insertComment(comment)==1){
            //加入缓存
            commentCache.insertComment(comment);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<Object> getReplyList(String commentId) {
        return commentCache.getReplyList(commentId);
    }
}
