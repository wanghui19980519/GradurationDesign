package club.simplecreate.cache;

import club.simplecreate.message.CommentMessage;
import club.simplecreate.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentCache {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @SuppressWarnings("unchecked")
    public void insertComment(Comment comment){
        //将该评论加入对应文章 list中,因为评论不可删，所以直接存入评论对象，而不是存入id
        redisTemplate.opsForList().leftPush("ARTICLE_COMMENTS:"+comment.getArticleId(),comment);
        //同时该作者的新消息数量加一，
        redisTemplate.opsForValue().increment("NEW_MESSAGE_NUMS:"+comment.getReceiverId());
        //并且将评论通知加入其通知列表
        CommentMessage message=new CommentMessage(comment);
        redisTemplate.opsForList().leftPush("COMMENT_MESSAGE:"+comment.getReceiverId(),message);
    }

    public List<Comment> getCommentList(String articleId) {
        List<Object> commentList= redisTemplate.opsForList().range("ARTICLE_COMMENTS:"+articleId,0,-1);
        List<Comment> res=new ArrayList<>();
        for(Object comment:commentList){
            Comment c=(Comment) comment;
            c.setReplyNums(redisTemplate.opsForList().size("COMMENT_REPLIES:"+c.getCommentId()));
            res.add(c);
        }
        return res;
    }

    public List<Object> getReplyList(String commentId) {
        return redisTemplate.opsForList().range("COMMENT_REPLIES:"+commentId,0,-1);
    }
}
