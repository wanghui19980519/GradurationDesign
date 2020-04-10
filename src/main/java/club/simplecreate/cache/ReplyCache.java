package club.simplecreate.cache;

import club.simplecreate.pojo.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReplyCache {
    @Autowired
    private RedisTemplate redisTemplate;

    @SuppressWarnings("unchecked")
    public void insertReply(Reply reply){
        //将该回复加入对应评论 list中,因为回复不可删，所以直接存入回复对象，而不是存入id
        redisTemplate.opsForList().leftPush("COMMENT_REPLIES:"+reply.getCommentId(),reply);
        //回复没有通知
    }
}
