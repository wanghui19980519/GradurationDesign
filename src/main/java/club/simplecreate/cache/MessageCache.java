package club.simplecreate.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageCache {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    public long getFollowMessageSize(String openId) {
        return redisTemplate.opsForList().size("NEW_LIKE_MESSAGES:"+openId);
    }

    public List<Object> getFollowMessage(String openId) {
        return redisTemplate.opsForList().range("NEW_LIKE_MESSAGES:"+openId,0,-1);
    }

    public List<Object> getCommentMessage(String openId) {
        return redisTemplate.opsForList().range("NEW_COMMENT_MESSAGES:"+openId,0,-1);
    }

    public long getCommentMessageSize(String openId) {
        return redisTemplate.opsForList().size("NEW_COMMENT_MESSAGES:"+openId);
    }

    public long getLikeMessageSize(String openId) {
        return redisTemplate.opsForList().size("NEW_LIKE_MESSAGES:"+openId);
    }

    public List<Object> getLikeMessage(String openId) {
        return redisTemplate.opsForList().range("NEW_LIKE_MESSAGES:"+openId,0,-1);
    }

    public void commentMessageAllRead(String openId){
        redisTemplate.delete("NEW_COMMENT_MESSAGES:"+openId);
    }

    public void likeMessageAllRead(String openId){
        redisTemplate.delete("NEW_LIKE_MESSAGES:"+openId);
    }

    public void followMessageAllRead(String openId){
        redisTemplate.delete("NEW_FOLLOW_MESSAGES:"+openId);
    }

}
