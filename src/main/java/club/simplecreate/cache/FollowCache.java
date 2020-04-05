package club.simplecreate.cache;

import club.simplecreate.message.FollowMessage;
import club.simplecreate.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class FollowCache {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public boolean follow(User user, String authorId) {
        long res;
        //判断该是否已关注
        if(redisTemplate.opsForSet().isMember("FOLLOWSET:"+authorId,user.getOpenid())){
            //已关注则取消关注
            res=redisTemplate.opsForSet().remove("FOLLOWSET:"+authorId,user.getOpenid());
        }
        else{
            //未关注
            //将该用户加入被关注用户的粉丝列表
            res=redisTemplate.opsForSet().add("FOLLOWSET:"+authorId,user.getOpenid());
            //关注信息加入信箱，未读消息加1
            redisTemplate.opsForValue().increment("NEWMESSAGENUMS:"+authorId);
            FollowMessage message=new FollowMessage(user);
            redisTemplate.opsForList().rightPush("NEWFOLLOWMESSAGES:"+authorId,message);
        }
        if(res==1){
            return true;
        }else{
            return false;
        }
    }
}
