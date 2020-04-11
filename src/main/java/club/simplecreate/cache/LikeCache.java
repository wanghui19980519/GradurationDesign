package club.simplecreate.cache;

import club.simplecreate.message.LikeMessage;
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
        return redisTemplate.opsForSet().isMember("ARTICLE_LIKES:"+articleId,openId);
    }

    public boolean like(User user, String authorId,String articleId, String title) {
        long res;
        //判断该是否已点赞
        if(isLike(user.getOpenid(),articleId)){
            //已关注则取消关注
            res=redisTemplate.opsForSet().remove("ARTICLE_LIKES:"+articleId,user.getOpenid());
        }
        else{
            //未关注
            //将该用户加入被关注用户的粉丝列表
            res=redisTemplate.opsForSet().add("ARTICLE_LIKES:"+articleId,user.getOpenid());
            //关注信息加入信箱，未读消息加1
            redisTemplate.opsForValue().increment("NEW_MESSAGE_NUMS:"+authorId);
            LikeMessage message=new LikeMessage(user,articleId,title);
            redisTemplate.opsForList().rightPush("NEW_LIKE_MESSAGES:"+authorId,message);
        }
        if(res==1){
            return true;
        }else{
            return false;
        }
    }
}
