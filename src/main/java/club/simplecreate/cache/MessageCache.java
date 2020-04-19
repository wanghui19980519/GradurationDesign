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
        return redisTemplate.opsForList().size("NEW_FOLLOW_MESSAGES:"+openId);
    }

    public List<Object> getFollowMessage(String openId) {
        return redisTemplate.opsForList().range("NEW_FOLLOW_MESSAGES:"+openId,0,-1);
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

    /**
     * 全部已读，并将消息加入已读列表
     * @param openId
     * @param num
     */
    public void commentMessageAllRead(String openId,int num){
        long size=redisTemplate.opsForList().size("NEW_COMMENT_MESSAGES:"+openId);
        if(size-num==0){
            redisTemplate.rename("NEW_COMMENT_MESSAGES:"+openId,"OLD_LIKE_MESSAGES:"+openId);
        }else {
            List<Object> res=redisTemplate.opsForList().range("NEW_COMMENT_MESSAGES:"+openId,size - num - 1,-1);
            redisTemplate.opsForList().trim("NEW_COMMENT_MESSAGES:" + openId, 0, size - num - 1);
            redisTemplate.opsForList().leftPushAll("OLD_COMMENT_MESSAGES:"+openId,res);
        }
    }
    /**
     * 全部已读，并将消息加入已读列表
     * @param openId
     * @param num
     */
    public void likeMessageAllRead(String openId,int num){
        long size=redisTemplate.opsForList().size("NEW_LIKE_MESSAGES:"+openId);
        if(size-num==0){
            redisTemplate.rename("NEW_LIKE_MESSAGES:"+openId,"OLD_LIKE_MESSAGES:"+openId);
        }else {
            List<Object> res=redisTemplate.opsForList().range("NEW_LIKE_MESSAGES:"+openId,size - num - 1,-1);
            redisTemplate.opsForList().trim("NEW_LIKE_MESSAGES:" + openId, 0, size - num - 1);
            redisTemplate.opsForList().leftPushAll("OLD_LIKE_MESSAGES:"+openId,res);
        }
    }
    /**
     * 全部已读，并将消息加入已读列表
     * @param openId
     * @param num
     */
    public void followMessageAllRead(String openId,int num){
        long size=redisTemplate.opsForList().size("NEW_FOLLOW_MESSAGES:"+openId);
        if(size-num==0){
            redisTemplate.rename("NEW_FOLLOW_MESSAGES:"+openId,"OLD_FOLLOW_MESSAGES:"+openId);
        }else {
            List<Object> res=redisTemplate.opsForList().range("NEW_FOLLOW_MESSAGES:"+openId,size - num - 1,-1);
            redisTemplate.opsForList().trim("NEW_FOLLOW_MESSAGES:" + openId, 0, size - num - 1);
            redisTemplate.opsForList().leftPushAll("OLD_FOLLOW_MESSAGES:"+openId,res);
        }
    }

    public void clearNewMessageNums(String openId){
        redisTemplate.opsForValue().set("NEW_MESSAGE_NUMS:"+openId,0);
    }
    public void readFollowMessageByIndex(String openId,int index){
        Object message =redisTemplate.opsForList().index("NEW_FOLLOW_MESSAGES:"+openId,index);
        long res=redisTemplate.opsForList().remove("NEW_FOLLOW_MESSAGES:"+openId,1,message);
        if(res==1){
            redisTemplate.opsForList().leftPush("OLD_FOLLOW_MESSAGES:"+openId,message);
        }
    }
    public void readCommentMessageByIndex(String openId, int index){
        Object message =redisTemplate.opsForList().index("NEW_COMMENT_MESSAGES:"+openId,index);
        long res=redisTemplate.opsForList().remove("NEW_COMMENT_MESSAGES:"+openId,1,message);
        if(res==1){
            redisTemplate.opsForList().leftPush("OLD_COMMENT_MESSAGES:"+openId,message);
        }
    }
    public void readLikeMessageByIndex(String openId, int index){
        Object message =redisTemplate.opsForList().index("NEW_LIKE_MESSAGES:"+openId,index);
        long res= redisTemplate.opsForList().remove("NEW_LIKE_MESSAGES:"+openId,1,message);
        if(res==1){
            redisTemplate.opsForList().leftPush("OLD_LIKE_MESSAGES:"+openId,message);
        }

    }
}
