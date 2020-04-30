package club.simplecreate.cache;

import club.simplecreate.pojo.Message;
import club.simplecreate.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class LikeCache {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public boolean isLike(String openId, String articleId) {
        //判断文章的点赞列表中是否存在该openidId
        return redisTemplate.opsForSet().isMember("USER_LIKE_ARTICLES:"+openId,articleId);
    }

    public void like(User user, String authorId,String articleId, String title) {
        //判断该是否已点赞
        if(isLike(user.getOpenid(),articleId)){
            redisTemplate.opsForSet().remove("ARTICLE_LIKES:"+articleId,user.getOpenid());
            redisTemplate.opsForSet().remove("USER_LIKE_ARTICLES:"+user.getOpenid(),articleId);
        }
        else{
            redisTemplate.opsForSet().add("ARTICLE_LIKES:"+articleId,user.getOpenid());
            redisTemplate.opsForSet().add("USER_LIKE_ARTICLES:"+user.getOpenid(),articleId);
            redisTemplate.opsForValue().increment("NEW_MESSAGE_NUMS:"+authorId);
            Message message=new Message(user,articleId,title);
            redisTemplate.opsForList().rightPush("NEW_LIKE_MESSAGES:"+authorId,message);
        }
    }
}
