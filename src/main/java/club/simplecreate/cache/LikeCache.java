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
    public boolean like(User user, String authorId,String articleId, String title) {
        long res;
        //判断该是否已点赞
        if(redisTemplate.opsForSet().isMember("LIKESET:"+user.getOpenid(),articleId)){
            //已关注则取消关注
            res=redisTemplate.opsForSet().remove("LIKESET:"+user.getOpenid(),articleId);
        }
        else{
            //未关注
            //将该用户加入被关注用户的粉丝列表
            res=redisTemplate.opsForSet().add("LIKESET:"+user.getOpenid(),articleId);
            //关注信息加入信箱，未读消息加1
            redisTemplate.opsForValue().increment("NEWMESSAGENUMS:"+authorId);
            LikeMessage message=new LikeMessage(user,articleId,title);
            redisTemplate.opsForList().rightPush("NEWLIKEMESSAGES:"+authorId,message);
        }
        if(res==1){
            return true;
        }else{
            return false;
        }
    }
}
