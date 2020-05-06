package club.simplecreate.cache;

import club.simplecreate.controller.WebSocket;
import club.simplecreate.pojo.Message;
import club.simplecreate.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LikeCache {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public boolean isLike(String openId, String articleId) {
        //判断文章的点赞列表中是否存在该openidId
        return redisTemplate.opsForSet().isMember("ARTICLE_LIKES:"+articleId,openId);
    }

    public void like(User user, String authorId,String articleId, String title) {
        //判断该是否已点赞
        if(isLike(user.getOpenid(),articleId)){
            redisTemplate.opsForSet().remove("ARTICLE_LIKES:"+articleId,user.getOpenid());
        }
        else{
            redisTemplate.opsForSet().add("ARTICLE_LIKES:"+articleId,user.getOpenid());
            redisTemplate.opsForValue().increment("NEW_MESSAGE_NUMS:"+authorId);
            Message message=new Message(user,articleId,title);
            redisTemplate.opsForList().rightPush("NEW_LIKE_MESSAGES:"+authorId,message);
            //判断用户是否在线，在线直接向其发送通知
            Map<String, WebSocket> map = WebSocket.webSocketSet;
            if(map.containsKey(authorId)){
                map.get(authorId).sendMessage(1);
            }
        }
    }
}
